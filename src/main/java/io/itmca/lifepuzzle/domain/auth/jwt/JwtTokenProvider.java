package io.itmca.lifepuzzle.domain.auth.jwt;

import io.itmca.lifepuzzle.domain.auth.Token;
import io.itmca.lifepuzzle.domain.auth.TokenType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

@Slf4j
public class JwtTokenProvider {

    private static final String JWT_SECRET = "secretkeyforlifepuzzleisrighthere";
    private static final Key SIGNING_KEY = getSigningKey();
    public static final int ACCESS_TOKEN_DURATION_MILLISECONDS = 30 * 60 * 1000;
    public static final int REFRESH_TOKEN_DURATION_MILLISECONDS = 12 * 24 * 60 * 60 * 1000;

    public static Token generateToken(Long userNo) {

        Date now = new Date();
        Date expiryDateOfAccessToken = new Date(now.getTime() + ACCESS_TOKEN_DURATION_MILLISECONDS);
        Date expiryDateOfRefreshToken = new Date(now.getTime() + REFRESH_TOKEN_DURATION_MILLISECONDS);

        String accessToken = Jwts.builder()
                .setClaims(new HashMap<>() {{
                    put("userNo", userNo);
                    put("type", TokenType.ACCESS);
                    put("iat", now);
                    put("exp", expiryDateOfAccessToken);
                }})
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();

        return Token.builder()
                .accessToken(accessToken)
                .accessTokenExpireAt(changeDateToLocalDateTime(expiryDateOfAccessToken))
                .refreshToken(generateRefreshToken(userNo, expiryDateOfRefreshToken))
                .refreshTokenExpireAt(changeDateToLocalDateTime(expiryDateOfRefreshToken))
                .build();
    }

    private static String generateRefreshToken(Long userNo, Date expiryDateOfRefreshToken) {

        Date now = new Date();

        return Jwts.builder()
                .setClaims(new HashMap<>() {{
                    put("userNo", userNo);
                    put("type", TokenType.REFRESH);
                    put("iat", now);
                    put("exp", expiryDateOfRefreshToken);
                }})
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static Long getUserNoFromJWT(String token) {
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
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    private static Key getSigningKey() {
        return Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    private static LocalDateTime changeDateToLocalDateTime(Date date) {
        return new Timestamp(date.getTime()).toLocalDateTime();
    }
}
