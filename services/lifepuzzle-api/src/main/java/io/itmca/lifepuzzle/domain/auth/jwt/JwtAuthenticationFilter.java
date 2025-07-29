package io.itmca.lifepuzzle.domain.auth.jwt;

import static io.itmca.lifepuzzle.domain.auth.jwt.JwtTokenProvider.toClaims;
import static org.springframework.util.StringUtils.hasText;

import io.itmca.lifepuzzle.domain.auth.type.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    try {
      trySettingAuthentication(request);
    } catch (JwtException e) {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      return;
    }

    filterChain.doFilter(request, response);
  }

  private void trySettingAuthentication(HttpServletRequest request) throws JwtException {
    var bearerToken = findBearerToken(request);
    var claims = toClaims(bearerToken).orElse(null);

    if (claims == null || !isAccessTokenType(claims)) {
      return;
    }

    var userNo = JwtTokenProvider.findUserNo(claims);
    var authentication = new UserAuthentication(userNo);
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  public boolean isAccessTokenType(Claims claims) {
    var tokenType = JwtTokenProvider.findTokenType(claims);

    return TokenType.ACCESS.frontEndKey().equals(tokenType);
  }

  private String findBearerToken(HttpServletRequest request) {
    var bearerToken = request.getHeader("Authorization");

    if (hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring("Bearer ".length());
    }

    return null;
  }
}
