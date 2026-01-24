package io.itmca.lifepuzzle.domain.auth.service;

import io.itmca.lifepuzzle.domain.auth.FacebookImage;
import io.itmca.lifepuzzle.domain.auth.FacebookPhoto;
import io.itmca.lifepuzzle.domain.auth.endpoint.response.FacebookPhotoDto;
import io.itmca.lifepuzzle.domain.auth.endpoint.response.FacebookPhotosResponse;
import io.itmca.lifepuzzle.domain.auth.oauth.response.FacebookPhotoResponse;
import java.util.Comparator;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class FacebookPhotoService {
  private final RestClient facebookRestClient;

  private static final int TARGET_HEIGHT = 1280;

  public FacebookPhotoResponse getUserPhotos(String accessToken) {
    var response = facebookRestClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/me/photos")
            .queryParam("type", "uploaded")
            .queryParam("fields", "images,name")
            .queryParam("access_token", accessToken)
            .build())
        .retrieve()
        .body(FacebookPhotoResponse.class);

    response.data().replaceAll(photo -> {
      var filtered = photo.images().stream()
          .filter(img -> img.height() == TARGET_HEIGHT)
          .toList();
      return new FacebookPhoto(filtered, photo.id());
    });

    return response;
  }

  public FacebookPhotosResponse getFilteredUserPhotos(String accessToken) {
    var response = getUserPhotos(accessToken);

    var filteredPhotos = response.data().stream()
        .map(this::selectBestImage)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(imageUrl -> new FacebookPhotoDto(imageUrl))
        .toList();

    return new FacebookPhotosResponse(filteredPhotos);
  }

  private Optional<String> selectBestImage(FacebookPhoto photo) {
    if (photo.images().isEmpty()) {
      return Optional.empty();
    }

    // 1. TARGET_HEIGHT와 정확히 일치하는 이미지 찾기
    var exactMatch = photo.images().stream()
        .filter(img -> img.height() == TARGET_HEIGHT)
        .findFirst();
    if (exactMatch.isPresent()) {
      return Optional.of(exactMatch.get().source());
    }

    // 2. TARGET_HEIGHT보다 큰 이미지 중 가장 작은 것 찾기
    var largerImage = photo.images().stream()
        .filter(img -> img.height() > TARGET_HEIGHT)
        .min(Comparator.comparingInt(FacebookImage::height));
    if (largerImage.isPresent()) {
      return Optional.of(largerImage.get().source());
    }

    // 3. TARGET_HEIGHT보다 작은 이미지 중 가장 큰 것 찾기
    var smallerImage = photo.images().stream()
        .filter(img -> img.height() < TARGET_HEIGHT)
        .max(Comparator.comparingInt(FacebookImage::height));
    if (smallerImage.isPresent()) {
      return Optional.of(smallerImage.get().source());
    }

    return Optional.empty();
  }
}
