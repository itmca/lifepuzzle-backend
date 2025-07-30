package io.itmca.lifepuzzle.domain.auth.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class UserAuthentication extends AbstractAuthenticationToken {

  private AuthPayload authPayload;

  public UserAuthentication(Long userNo) {
    super(null);
    this.authPayload = new AuthPayload(userNo);
    setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public AuthPayload getPrincipal() {
    return authPayload;
  }
}
