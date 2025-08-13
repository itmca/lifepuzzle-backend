package io.itmca.lifepuzzle.domain.auth.service;

import io.itmca.lifepuzzle.domain.auth.endpoint.response.FacebookPhotoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FacebookPhotoService {
  private final WebClient webClient;

  private static final int TARGET_HEIGHT = 1280;

  public Mono<FacebookPhotoResponse> getUserPhotos(String accessToken) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/me/photos")
            .queryParam("type", "uploaded")
            .queryParam("fields", "images,name")
            .queryParam("access_token", accessToken)
            .build())
        .retrieve()
        .bodyToMono(FacebookPhotoResponse.class)
        .map(response -> {
          response.getData().forEach(photo -> {
            if (photo.getImages() != null) {
              photo.setImages(
                  photo.getImages().stream()
                      .filter(img -> img.getHeight() == TARGET_HEIGHT)
                      .toList()
              );
            }
          });
          return response;
        });
  }
}
