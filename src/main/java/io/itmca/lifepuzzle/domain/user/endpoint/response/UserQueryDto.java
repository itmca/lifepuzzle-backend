package io.itmca.lifepuzzle.domain.user.endpoint.response;

import io.itmca.lifepuzzle.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public
class UserQueryDto {
    /*
    [Debugging]
        DTO가 userQueryEndpoint에서 별도로 사용되어야 해서 밖으로 뺌
     */
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
