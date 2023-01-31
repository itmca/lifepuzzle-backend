package io.itmca.lifepuzzle.domain.auth.jwt;

import io.itmca.lifepuzzle.domain.auth.Token;
import io.itmca.lifepuzzle.domain.auth.TokenType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@Slf4j
public class JwtTokenProvider {

    private static final String JWT_SECRET = "secretkeyforlifepuzzleisrighthere";
    private static final Key SIGNING_KEY = getSigningKey();
    public static final int ACCESS_TOKEN_DURATION_SECONDS = 60 * 30;
    public static final int REFRESH_TOKEN_DURATION_SECONDS = 60 * 60 * 24 * 14;

    public static Token generateToken(Long userNo) {
        var now = Instant.now();
        var expiryDateOfAccessToken = now.plusSeconds(ACCESS_TOKEN_DURATION_SECONDS);
        var expiryDateOfRefreshToken = now.plusSeconds(REFRESH_TOKEN_DURATION_SECONDS);

        var accessToken = Jwts.builder()
                .setClaims(Map.of(
                        "userNo", userNo,
                        "type", TokenType.ACCESS.frontEndKey(),
                        "iat", now.getEpochSecond(),
                        "exp", expiryDateOfAccessToken.getEpochSecond()
                ))
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();

        return Token.builder()
                .accessToken(accessToken)
                .accessTokenExpireAt(LocalDateTime.ofInstant(expiryDateOfAccessToken, ZoneId.of("Asia/Seoul")))
                .refreshToken(generateRefreshToken(userNo, expiryDateOfRefreshToken))
                .refreshTokenExpireAt(LocalDateTime.ofInstant(expiryDateOfRefreshToken, ZoneId.of("Asia/Seoul")))
                .build();
    }

    private static String generateRefreshToken(Long userNo, Instant expiryDateOfRefreshToken) {

        return Jwts.builder()
                .setClaims(Map.of(
                        "userNo", userNo,
                        "type", TokenType.REFRESH.frontEndKey(),
                        "iat", Instant.now().getEpochSecond(),
                        "exp", expiryDateOfRefreshToken.getEpochSecond()
                ))
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static String parseTokenType(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("type", String.class);
    }

    public static Long parseUserNo(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userNo", Long.class);
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature", ex);
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token", ex);
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token", ex);
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token", ex);
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.", ex);
        }
        return false;
    }

    public static Key getSigningKey() {
        return Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }
}
