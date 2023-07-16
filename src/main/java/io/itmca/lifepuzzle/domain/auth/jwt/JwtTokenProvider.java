package io.itmca.lifepuzzle.domain.auth.jwt;

import static org.springframework.util.StringUtils.hasText;

import io.itmca.lifepuzzle.domain.auth.type.TokenPayload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtTokenProvider {

  private static final String JWT_SECRET = "secretkeyforlifepuzzleisrighthere";
  private static final Key SIGNING_KEY = getSigningKey();

  public static String generateToken(Map payload) {
    return Jwts.builder()
        .setClaims(payload)
        .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
        .compact();
  }

  public static Optional<Claims> toClaims(String token) {
    if (!hasText(token)) {
      return Optional.empty();
    }
    
    try {
      return Optional.of(Jwts.parserBuilder()
          .setSigningKey(SIGNING_KEY)
          .build()
          .parseClaimsJws(token)
          .getBody());
    } catch (SignatureException ex) {
      log.debug("Invalid JWT signature", ex);
    } catch (MalformedJwtException ex) {
      log.debug("Invalid JWT token", ex);
    } catch (ExpiredJwtException ex) {
      log.debug("Expired JWT token", ex);
    } catch (UnsupportedJwtException ex) {
      log.debug("Unsupported JWT token", ex);
    } catch (IllegalArgumentException ex) {
      log.debug("JWT claims string is empty.", ex);
    }

    return Optional.empty();
  }

  public static String findTokenType(Claims claims) {
    return claims.get(TokenPayload.Type.key(), String.class);
  }

  public static Long findUserNo(Claims claims) {
    return claims.get(TokenPayload.UserNo.key(), Long.class);
  }

  public static Key getSigningKey() {
    return Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
  }
}
