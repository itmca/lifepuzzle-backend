package io.itmca.lifepuzzle.domain.user.endpoint.response.dto;

import static io.itmca.lifepuzzle.global.constants.FileConstant.USER_PROFILE_IMAGE_BASE_PATH_FORMAT;
import static io.itmca.lifepuzzle.global.constants.ServerConstant.S3_SERVER_HOST;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.itmca.lifepuzzle.domain.user.entity.User;
import java.time.LocalDate;
import org.apache.commons.lang3.StringUtils;

public record UserQueryDto(
    Long id,
    String loginId,
    String nickName,
    Long recentHeroNo,
    String userType,
    String email,
    LocalDate birthday,
    String imageUrl
) {

  public static UserQueryDto from(User user) {
    return new UserQueryDto(
        user.getId(),
        user.getLoginId(),
        user.getNickName(),
        user.getRecentHeroNo(),
        user.getUserType(),
        user.getEmail(),
        user.getBirthday(),
        addServerHostInImage(user.getId(), user.getImage())
    );
  }

  /**
   * Returns user ID for backward compatibility.
   *
   * @deprecated Use {@link #id()} instead. Will be removed after FE migration.
   */
  @Deprecated
  @JsonProperty("userNo")
  public Long userNo() {
    return id;
  }

  /**
   * Returns login ID for backward compatibility.
   *
   * @deprecated Use {@link #loginId()} instead. Will be removed after FE migration.
   */
  @Deprecated
  @JsonProperty("userId")
  public String userId() {
    return loginId;
  }

  /**
   * Returns user nickname for backward compatibility.
   *
   * @deprecated Use {@link #nickName()} instead. Will be removed after FE migration.
   */
  @Deprecated
  @JsonProperty("userNickName")
  public String userNickName() {
    return nickName;
  }

  private static String addServerHostInImage(Long userNo, String imageUrl) {
    if (StringUtils.isBlank(imageUrl)) {
      return "";
    }

    return S3_SERVER_HOST + USER_PROFILE_IMAGE_BASE_PATH_FORMAT.formatted(userNo) + imageUrl;
  }
}
