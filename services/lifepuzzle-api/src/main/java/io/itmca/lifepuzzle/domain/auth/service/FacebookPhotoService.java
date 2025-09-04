package io.itmca.lifepuzzle.domain.auth.service;

import io.itmca.lifepuzzle.domain.auth.FacebookPhoto;
import io.itmca.lifepuzzle.domain.auth.endpoint.response.FacebookPhotoResponse;
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
}
