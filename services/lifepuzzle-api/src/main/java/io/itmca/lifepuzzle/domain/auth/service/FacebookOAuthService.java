package io.itmca.lifepuzzle.domain.auth.service;

import io.itmca.lifepuzzle.domain.auth.oauth.response.FacebookTokenResponse;
import io.itmca.lifepuzzle.domain.auth.oauth.response.FacebookUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class FacebookOAuthService {
  private final RestClient restClient;

  @Value("${facebook.client-id}")
  private String clientId;
  @Value("${facebook.client-secret}")
  private String clientSecret;
  @Value("${facebook.redirect-uri}")
  private String redirectUri;

  private static final String TOKEN_PATH = "/oauth/access_token";
  private static final String ME_PATH = "/me";

  public String getAccessToken(String code) {
    return restClient.get()
        .uri(uriBuilder -> uriBuilder
            .path(TOKEN_PATH)
            .queryParam("client_id", clientId)
            .queryParam("redirect_uri", redirectUri)
            .queryParam("client_secret", clientSecret)
            .queryParam("code", code)
            .build())
        .retrieve()
        .body(FacebookTokenResponse.class)
        .accessToken();
  }

  public String getUserId(String accessToken) {
    return restClient.get()
        .uri(uriBuilder -> uriBuilder
            .path(ME_PATH)
            .queryParam("fields", "id")
            .queryParam("access_token", accessToken)
            .build())
        .retrieve()
        .body(FacebookUserResponse.class)
        .id();
  }
}
