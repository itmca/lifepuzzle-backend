package io.itmca.lifepuzzle.domain.auth.endpoint.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.itmca.lifepuzzle.domain.auth.Token;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.HeroQueryDTO;
import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
    private UserQueryDto user;
    private TokenQueryDto tokens;
    /* [Debugging]
        클라이언트 입장에서 hero 키 값으로 한 번 더 랩핑 되어 있으면 암됨 HeroQeuryResponse -> HeroQueryDTO
     */
    private HeroQueryDTO hero;
    private Boolean isNewUser;

    public static LoginResponse from(User user, Token tokens, Hero hero) {
        return LoginResponse.builder()
                .user(UserQueryDto.from(user))
                .tokens(TokenQueryDto.from(tokens))
                .hero(HeroQueryDTO.from(hero))
                .build();
    }

    public LoginResponse from(boolean isNewUser) {
        this.isNewUser = isNewUser;
        return this;
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
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
                    .socialToken(tokens.getSocialToken())
                    .build();
        }
    }
}
