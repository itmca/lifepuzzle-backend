package io.itmca.lifepuzzle.domain.user.endpoint.response.dto;

import static io.itmca.lifepuzzle.global.constants.FileConstant.USER_PROFILE_IMAGE_BASE_PATH_FORMAT;
import static io.itmca.lifepuzzle.global.constants.ServerConstant.S3_SERVER_HOST;

import io.itmca.lifepuzzle.domain.user.entity.User;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class UserQueryDto {
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
        .userNo(user.getId())
        .userId(user.getLoginId())
        .userNickName(user.getNickName())
        .recentHeroNo(user.getRecentHeroNo())
        .email(user.getEmail())
        .birthday(user.getBirthday())
        .userType(user.getUserType())
        .imageURL(addServerHostInImage(user.getId(), user.getImage()))
        .build();
  }

  private static String addServerHostInImage(Long userNo, String imageURL) {
    if (StringUtils.isBlank(imageURL)) {
      return "";
    }

    return S3_SERVER_HOST + USER_PROFILE_IMAGE_BASE_PATH_FORMAT.formatted(userNo) + imageURL;
  }
}
