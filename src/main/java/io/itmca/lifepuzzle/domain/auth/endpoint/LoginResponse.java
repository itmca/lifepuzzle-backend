package io.itmca.lifepuzzle.domain.auth.endpoint;

import io.itmca.lifepuzzle.domain.auth.entity.Token;
import io.itmca.lifepuzzle.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class LoginResponse {

    private UserQueryDto user;
    private TokenQueryDto tokens;

    public static LoginResponse from(User user, Token tokens) {
        return LoginResponse.builder()
                .user(UserQueryDto.from(user))
                .tokens(TokenQueryDto.from(tokens))
                .build();
    }

    @Getter
    @Builder
    private static class UserQueryDto {
        private Long userNo;
        private String userNickName;
        private String userType;

        public static UserQueryDto from(User user) {
//            return UserQueryDto.builder()
//                    .userNo(user.getUserNo())
//                    .userNickName(user.getNickName())
//                    .userType(user.getUserType(user))
//                    .build();
            return UserQueryDto.builder()
                    .userNo(1L)
                    .userNickName("솔미")
                    .userType("")
                    .build();
        }
    }

    @Getter
    @Builder
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
                    .refreshToken("")
                    .refreshTokenExpireAt(tokens.getRefreshTokenExpireAt())
                    .socialToken("")
                    .build();
        }
    }


}
