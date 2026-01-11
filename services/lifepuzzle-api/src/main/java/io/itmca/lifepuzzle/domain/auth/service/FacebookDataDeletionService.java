package io.itmca.lifepuzzle.domain.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.itmca.lifepuzzle.domain.auth.service.dto.FacebookSignedRequestPayload;
import io.itmca.lifepuzzle.domain.content.repository.GalleryRepository;
import io.itmca.lifepuzzle.domain.content.type.GallerySource;
import io.itmca.lifepuzzle.domain.hero.repository.HeroRepository;
import io.itmca.lifepuzzle.global.exception.InvalidSignedRequestException;
import io.itmca.lifepuzzle.global.file.service.S3UploadService;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FacebookDataDeletionService {
  private static final String SIGNATURE_ALGORITHM = "HmacSHA256";
  private static final String EXPECTED_ALGORITHM = "HMAC-SHA256";

  private final ObjectMapper objectMapper;
  private final HeroRepository heroRepository;
  private final GalleryRepository galleryRepository;
  private final S3UploadService s3UploadService;

  @Value("${facebook.client-secret}")
  private String clientSecret;

  @Transactional
  public String processSignedRequest(String signedRequest) {
    if (signedRequest == null || signedRequest.isBlank()) {
      throw new InvalidSignedRequestException();
    }

    var parts = signedRequest.split("\\.", 2);
    if (parts.length != 2) {
      throw new InvalidSignedRequestException();
    }

    var signature = decodeUrlBase64(parts[0]);
    var payloadBytes = decodeUrlBase64(parts[1]);
    var expectedSignature = hmacSha256(payloadBytes);

    if (!MessageDigest.isEqual(signature, expectedSignature)) {
      throw new AccessDeniedException("Invalid signed_request signature.");
    }

    var payload = parsePayload(payloadBytes);
    if (!EXPECTED_ALGORITHM.equals(payload.algorithm())) {
      throw new AccessDeniedException("Unsupported signed_request algorithm.");
    }

    if (payload.userId() == null || payload.userId().isBlank()) {
      throw new InvalidSignedRequestException();
    }

    deleteFacebookData(payload.userId());

    return generateConfirmationCode(payload.userId());
  }

  private byte[] decodeUrlBase64(String encoded) {
    try {
      return Base64.getUrlDecoder().decode(encoded);
    } catch (IllegalArgumentException e) {
      throw new InvalidSignedRequestException();
    }
  }

  private byte[] hmacSha256(byte[] payload) {
    try {
      var mac = Mac.getInstance(SIGNATURE_ALGORITHM);
      var keySpec = new SecretKeySpec(clientSecret.getBytes(StandardCharsets.UTF_8), SIGNATURE_ALGORITHM);
      mac.init(keySpec);
      return mac.doFinal(payload);
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      throw new IllegalStateException("Failed to verify signed_request.", e);
    }
  }

  private FacebookSignedRequestPayload parsePayload(byte[] payloadBytes) {
    try {
      return objectMapper.readValue(payloadBytes, FacebookSignedRequestPayload.class);
    } catch (Exception e) {
      throw new InvalidSignedRequestException();
    }
  }

  private String generateConfirmationCode(String userId) {
    var source = userId + ":" + UUID.randomUUID();
    return UUID.nameUUIDFromBytes(source.getBytes(StandardCharsets.UTF_8)).toString();
  }

  private void deleteFacebookData(String facebookUserId) {
    var heroOpt = heroRepository.findByFacebookUserId(facebookUserId);
    if (heroOpt.isEmpty()) {
      return;
    }

    var hero = heroOpt.get();
    var galleries = galleryRepository.findAllByHeroIdAndSource(hero.getHeroNo(), GallerySource.FACEBOOK);
    for (var gallery : galleries) {
      if (gallery.getUrl() != null && !gallery.getUrl().isBlank()) {
        s3UploadService.delete(gallery.getUrl());
      }
    }
    galleryRepository.deleteAll(galleries);

    hero.setFacebookUserId(null);
  }
}
