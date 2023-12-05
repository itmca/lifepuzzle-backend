package io.itmca.lifepuzzle.domain.user.endpoint.response;

import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.global.constant.ServerConstant;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class UserQueryDto {
  // TODO UserQueryDTO와 비교 필요
  private Long userNo;
  private String userId;
  private String userNickName;
  private Long recentHeroNo;
  private String userType;
  private String email;
  private LocalDate birthday;
  private String imageURL;

  public static UserQueryDto from(User user) {
    return UserQueryDto.builder()
        .userNo(user.getUserNo())
        .userId(user.getUserId())
        .userNickName(user.getNickName())
        .recentHeroNo(user.getRecentHeroNo())
        .email(user.getEmail())
        .birthday(user.getBirthday())
        .userType(user.getUserType())
        .imageURL(addServerHostInImage(user.getUserNo(), user.getImage()))
        .build();
  }

  private static String addServerHostInImage(Long userNo, String imageURL) {
    if (imageURL == null || imageURL.trim().equals("")) {
      return String.format("%s/user/profile/default.jpg", ServerConstant.SERVER_HOST);
    }

    return String.format("%s/user/profile/%d/image/%s", ServerConstant.SERVER_HOST, userNo,
        imageURL);
  }
}
