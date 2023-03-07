package io.itmca.lifepuzzle.domain.user.endpoint.response;

import io.itmca.lifepuzzle.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserQueryResponse {

    private UserQueryDto user;

    public static UserQueryResponse from(User user) {
        return UserQueryResponse.builder()
                .user(UserQueryDto.from(user))
                .build();
    }

}
