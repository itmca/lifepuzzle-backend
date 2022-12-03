package io.itmca.lifepuzzle.domain.auth.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
public class JwtTokenProvider {

    private static final String JWT_SECRET = "secretkey";

    public static final int ACCESS_TOKEN_DURATION = 30 * 60;
    public static final int REFRESH_TOKEN_DURATION = 12 * 24 * 60 * 60;

    public static String generateToken(Authentication authentication) {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plusSeconds(ACCESS_TOKEN_DURATION);

        return Jwts.builder()
                .setSubject((String) authentication.getPrincipal())
                .setIssuedAt(Timestamp.valueOf(now))
                .setExpiration(Timestamp.valueOf(expiryDate))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .compact();
    }

    public static String getUserIdFromJWT(String token) {
        return Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
            // 여기에 refresh token 확인 코드 추가
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
}
