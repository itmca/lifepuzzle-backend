package io.itmca.lifepuzzle.domain.auth.endpoint.response;

import io.itmca.lifepuzzle.domain.auth.Token;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.HeroQueryResponse;
import io.itmca.lifepuzzle.domain.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginResponse {

    private UserQueryDto user;
    private TokenQueryDto tokens;
    private HeroQueryResponse hero;

    public static LoginResponse from(User user, Token tokens, HeroQueryResponse hero) {
        return LoginResponse.builder()
                .user(UserQueryDto.from(user))
                .tokens(TokenQueryDto.from(tokens))
                .hero(hero)
                .build();
    }

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    private static class UserQueryDto {
        private Long userNo;
        private String userNickName;
        private String userType;

        public static UserQueryDto from(User user) {
            return UserQueryDto.builder()
                    .userNo(user.getUserNo())
                    .userNickName(user.getNickName())
                    .userType(user.getUserType())
                    .build();
        }
    }

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    private static class TokenQueryDto {
        private String accessToken;
        private LocalDateTime accessTokenExpireAt;
        private String refreshToken;
        private LocalDateTime refreshTokenExpireAt;
        private String socialToken;

        public static TokenQueryDto from(Token tokens) {
            return TokenQueryDto.builder()
                    .accessToken(tokens.getAccessToken())
                    .accessTokenExpireAt(tokens.getAccessTokenExpireAt())
                    .refreshToken(tokens.getRefreshToken())
                    .refreshTokenExpireAt(tokens.getRefreshTokenExpireAt())
                    .socialToken("")
                    .build();
        }
    }
}
