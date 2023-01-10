package io.itmca.lifepuzzle.domain.user.endpoint.response;

import io.itmca.lifepuzzle.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class UserQueryResponse {

    private UserQueryDto user;

    public static UserQueryResponse from(User user) {
        return UserQueryResponse.builder()
                .user(UserQueryDto.from(user))
                .build();
    }

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    private static class UserQueryDto {
        private Long userNo;
        private String userId;
        private String userNickName;
        private Long recentHeroNo;
        private String userType;
        private String email;
        private LocalDate birthday;

        public static UserQueryDto from(User user) {
            return UserQueryDto.builder()
                    .userNo(user.getUserNo())
                    .userId(user.getUserId())
                    .userNickName(user.getNickName())
                    .recentHeroNo(user.getRecentHeroNo())
                    .email(user.getEmail())
                    .birthday(user.getBirthday())
                    .userType(user.getUserType())
                    .build();
        }
    }
}
