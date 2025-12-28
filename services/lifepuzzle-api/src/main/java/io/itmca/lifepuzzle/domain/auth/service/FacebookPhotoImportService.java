package io.itmca.lifepuzzle.domain.auth.service;

import io.itmca.lifepuzzle.domain.auth.endpoint.response.FacebookPhotoImportResponse;
import io.itmca.lifepuzzle.domain.content.service.GalleryWriteService;
import io.itmca.lifepuzzle.domain.content.service.GalleryWriteService.FacebookImportPhoto;
import io.itmca.lifepuzzle.domain.content.type.AgeGroup;
import io.itmca.lifepuzzle.domain.user.repository.UserRepository;
import io.itmca.lifepuzzle.global.exception.FacebookUserAlreadyLinkedException;
import io.itmca.lifepuzzle.global.exception.MissingHeroNoException;
import io.itmca.lifepuzzle.global.exception.UserNotFoundException;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class FacebookPhotoImportService {
  private final FacebookOAuthService facebookOAuthService;
  private final FacebookPhotoService facebookPhotoService;
  private final GalleryWriteService galleryWriteService;
  private final UserRepository userRepository;

  @Transactional
  public FacebookPhotoImportResponse importPhotos(Long userId, String code, Long heroNo, AgeGroup ageGroup) {
    if (heroNo == null) {
      throw new MissingHeroNoException();
    }

    var accessToken = facebookOAuthService.getAccessToken(code);
    var facebookUserId = facebookOAuthService.getUserId(accessToken);

    userRepository.findByFacebookUserId(facebookUserId)
        .filter(existing -> !existing.getId().equals(userId))
        .ifPresent(existing -> {
          throw new FacebookUserAlreadyLinkedException(facebookUserId);
        });

    var user = userRepository.findById(userId)
        .orElseThrow(() -> UserNotFoundException.byUserNo(userId));
    user.setFacebookUserId(facebookUserId);

    var photosResponse = facebookPhotoService.getFilteredUserPhotos(accessToken);
    List<FacebookImportPhoto> photos = photosResponse.photos().stream()
        .map(photo -> toImportPhoto(photo.imageUrl()))
        .toList();

    var saved = galleryWriteService.saveFacebookGallery(heroNo, userId, photos, ageGroup);
    return new FacebookPhotoImportResponse(saved.size());
  }

  private FacebookImportPhoto toImportPhoto(String imageUrl) {
    var bytes = RestClient.create().get()
        .uri(imageUrl)
        .retrieve()
        .body(byte[].class);
    if (bytes == null) {
      return new FacebookImportPhoto("download-failed.jpg", new byte[0]);
    }

    return new FacebookImportPhoto(resolveFileName(imageUrl), bytes);
  }

  private String resolveFileName(String imageUrl) {
    try {
      var uri = URI.create(imageUrl);
      var path = uri.getPath();
      if (path == null || path.isBlank()) {
        return "facebook.jpg";
      }

      var fileName = path.substring(path.lastIndexOf('/') + 1);
      if (!fileName.contains(".")) {
        return fileName + ".jpg";
      }
      return fileName;
    } catch (Exception e) {
      return "facebook.jpg";
    }
  }
}
