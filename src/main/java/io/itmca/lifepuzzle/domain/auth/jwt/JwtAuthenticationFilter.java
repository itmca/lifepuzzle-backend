package io.itmca.lifepuzzle.domain.auth.jwt;

import static io.itmca.lifepuzzle.domain.auth.jwt.JwtTokenProvider.validateToken;
import static org.springframework.util.StringUtils.hasText;

import io.itmca.lifepuzzle.domain.auth.TokenType;
import io.itmca.lifepuzzle.global.exception.TokenTypeMismatchException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    try {
      trySettingAuthentication(request);
    } catch (Exception ex) {
      logger.error("Could not set user authentication in security context", ex);
    }

    filterChain.doFilter(request, response);
  }

  private void trySettingAuthentication(HttpServletRequest request) {
    var jwt = getJwtFromRequest(request);

    if (!hasText(jwt) || !validateToken(jwt)) {
      return;
    }

    if (!isAccessToken(jwt)) {
      throw TokenTypeMismatchException.accessTokenExpected(jwt);
    }

    var userNo = JwtTokenProvider.parseUserNo(jwt);

    var authentication = new UserAuthentication(userNo);
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  private boolean isAccessToken(String token) {
    return TokenType.ACCESS.frontEndKey().equals(JwtTokenProvider.parseTokenType(token));
  }

  private String getJwtFromRequest(HttpServletRequest request) {
    var bearerToken = request.getHeader("Authorization");
    if (hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring("Bearer ".length());
    }
    return null;
  }

}
