package io.itmca.lifepuzzle.domain.auth.service;

import io.itmca.lifepuzzle.domain.auth.endpoint.response.FacebookTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FacebookOAuthService {
  private final WebClient webClient;

  @Value("${facebook.client-id}")
  private String clientId;
  @Value("${facebook.client-secret}")
  private String clientSecret;
  @Value("${facebook.redirect-uri}")
  private String redirectUri;

  private static final String TOKEN_PATH = "/oauth/access_token";

  public Mono<String> getAccessToken(String code) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path(TOKEN_PATH)
            .queryParam("client_id", clientId)
            .queryParam("redirect_uri", redirectUri)
            .queryParam("client_secret", clientSecret)
            .queryParam("code", code)
            .build())
        .retrieve()
        .bodyToMono(FacebookTokenResponse.class)
        .map(FacebookTokenResponse::getAccessToken);
  }
}

