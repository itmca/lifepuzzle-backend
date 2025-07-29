package io.itmca.lifepuzzle.domain.auth.jwt;

import static org.springframework.util.StringUtils.hasText;

import io.itmca.lifepuzzle.domain.auth.type.TokenPayload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtTokenProvider {

  private static final String JWT_SECRET = "secretkeyforlifepuzzleisrighthere";
  private static final SecretKey SIGNING_KEY = getSigningKey();

  public static String generateToken(Map<String, ?> payload) {
    return Jwts.builder()
        .claims(payload)
        .signWith(SIGNING_KEY, Jwts.SIG.HS256)
        .compact();
  }

  public static Optional<Claims> toClaims(String token) {
    if (!hasText(token)) {
      return Optional.empty();
    }

    try {
      return Optional.of((Claims) Jwts.parser()
          .verifyWith(SIGNING_KEY)
          .build()
          .parse(token)
          .getPayload());
    } catch (SignatureException ex) {
      throw new JwtException("잘못된 JWT 서명입니다.");
    } catch (MalformedJwtException | IllegalArgumentException ex) {
      throw new JwtException("잘못된 JWT 토큰입니다.");
    } catch (ExpiredJwtException ex) {
      throw new JwtException("만료된 JWT 토큰입니다.");
    } catch (UnsupportedJwtException ex) {
      throw new JwtException("지원되지 않는 JWT 토큰입니다.");
    }
  }

  public static String findTokenType(Claims claims) {
    return claims.get(TokenPayload.Type.key(), String.class);
  }

  public static Long findUserNo(Claims claims) {
    return claims.get(TokenPayload.UserNo.key(), Long.class);
  }

  public static SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
  }
}
