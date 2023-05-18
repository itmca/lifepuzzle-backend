package io.itmca.lifepuzzle.domain.auth.jwt;

import io.itmca.lifepuzzle.domain.auth.TokenType;
import io.itmca.lifepuzzle.global.exception.TokenTypeMismatchException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    try {
      var jwt = getJwtFromRequest(request);
      var servletPath = request.getServletPath();

      if (servletPath.equals("/auth/login") || servletPath.equals("/user")) {
        filterChain.doFilter(request, response);
        return;
      }

      if (StringUtils.hasText(jwt) && JwtTokenProvider.validateToken(jwt)) {
        var tokenType = JwtTokenProvider.parseTokenType(jwt);
        if (!isAccessToken(tokenType)) {
          throw new TokenTypeMismatchException(tokenType);
        }

        var userNo = JwtTokenProvider.parseUserNo(jwt);
        var authentication = new UserAuthentication(userNo);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
      } else {
        if (!StringUtils.hasText(jwt) && !JwtTokenProvider.validateToken(jwt)) {
          request.setAttribute("unauthorization", "401 인증키 없음.");
        } else {
          request.setAttribute("unauthorization", "401-001 인증키 만료.");
        }
      }
    } catch (Exception ex) {
      logger.error("Could not set user authentication in security context", ex);
    }

    filterChain.doFilter(request, response);
  }

  private static boolean isAccessToken(String tokenType) {
    return TokenType.ACCESS.frontEndKey().equals(tokenType);
  }

  private String getJwtFromRequest(HttpServletRequest request) {
    var bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring("Bearer ".length());
    }
    return null;
  }

}
