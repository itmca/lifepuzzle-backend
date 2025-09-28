package io.itmca.lifepuzzle.domain.auth.service;

import io.itmca.lifepuzzle.domain.auth.oauth.response.FacebookTokenResponse;
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
}

