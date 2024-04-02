package io.itmca.lifepuzzle.domain.register.service;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import io.itmca.lifepuzzle.domain.register.endpoint.request.UserWithdrawRequest;
import io.itmca.lifepuzzle.domain.user.UserType;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.domain.user.service.UserWriteService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WithdrawService {

  private final UserWriteService userWriteService;

  public void withdraw(User user, UserWithdrawRequest userWithdrawRequest) {
    var socialToken = getSocialToken(userWithdrawRequest);

    if (UserType.APPLE.frontEndKey().equals(user.getUserType())) {
      if (isEmpty(socialToken)) {
        throw new IllegalArgumentException("socialToken is required for an Apple user.");
      }

      try {
        revokeAppleToken(socialToken);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    userWriteService.deleteByUserNo(user.getUserNo());
  }

  private String getSocialToken(UserWithdrawRequest userWithdrawRequest) {
    return Optional.ofNullable(userWithdrawRequest)
        .map(UserWithdrawRequest::getSocialToken)
        .orElse(null);
  }

  private void revokeAppleToken(String socialToken) throws IOException {
    var url = new URL("https://appleid.apple.com/auth/revoke");
    var conn = (HttpURLConnection) url.openConnection();

    conn.setRequestMethod("POST");
    conn.setDoOutput(true);
    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

    var body = new JSONObject();

    body.put("client_id", "io.itmca.lifepuzzle");
    body.put("client_secret", getAppleSecret());
    body.put("token", socialToken);

    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
    bw.write(body.toString());
    bw.flush();
    bw.close();

    int responseCode = conn.getResponseCode();

    if (responseCode != 200) {
      // TODO empty if 처리 필요
    }
  }

  private String getAppleSecret() {
    var appleTeamId = "***REMOVED***";
    var appleBundleId = "io.itmca.lifepuzzle";
    var applePrivateKeyId = "***REMOVED***";

    var nowInSeconds = Instant.now().getEpochSecond();
    var durationInSeconds = 60 * 5;

    return Jwts.builder()
        .setHeaderParams(Map.of(
            "algorithm", "ES256",
            "keyId", applePrivateKeyId
        ))
        .setClaims(Map.of(
            "iss", appleTeamId,
            "iat", nowInSeconds,
            "exp", nowInSeconds + durationInSeconds,
            "aud", "https://appleid.apple.com",
            "sub", appleBundleId
        ))
        .signWith(getApplePrivateKey(), SignatureAlgorithm.ES256)
        .compact();
  }

  private PrivateKey getApplePrivateKey() {
    var privateKey = "***REMOVED***"
        + "***REMOVED***"
        + "***REMOVED***"
        + "***REMOVED***"
        + "***REMOVED***"
        + "***REMOVED***";

    privateKey = privateKey.replace("***REMOVED***", "");
    privateKey = privateKey.replace("***REMOVED***", "");

    try {
      var keyFactory = KeyFactory.getInstance("EC");

      return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(
          Base64.decodeBase64(privateKey)));
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new RuntimeException(e);
    }
  }
}
