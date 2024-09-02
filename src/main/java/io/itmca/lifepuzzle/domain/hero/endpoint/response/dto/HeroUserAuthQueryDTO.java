package io.itmca.lifepuzzle.domain.hero.endpoint.response.dto;

import static io.itmca.lifepuzzle.global.constant.FileConstant.USER_PROFILE_IMAGE_BASE_PATH_FORMAT;
import static io.itmca.lifepuzzle.global.constant.ServerConstant.S3_SERVER_HOST;

import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;


@Getter
@Builder
@Schema(title = "유저 주인공 권한 조회 DTO")
public class HeroUserAuthQueryDTO {
  @Schema(description = "유저키")
  private Long userNo;
  @Schema(description = "별칭")
  private String nickName;
  @Schema(description = "대표이미지")
  private String imageURL;
  @Schema(description = "권한")
  private HeroAuthStatus auth;

  public static HeroUserAuthQueryDTO from(HeroUserAuth heroUserAuth) {
    var user = heroUserAuth.getUser();
    return HeroUserAuthQueryDTO.builder()
        .userNo(user.getUserNo())
        .nickName(user.getNickName())
        .imageURL(addServerHostInImage(user.getUserNo(), user.getImage()))
        .auth(heroUserAuth.getAuth())
        .build();
  }

  private static String addServerHostInImage(Long userNo, String imageURL) {
    if (StringUtils.isBlank(imageURL)) {
      return "";
    }

    return S3_SERVER_HOST + USER_PROFILE_IMAGE_BASE_PATH_FORMAT.formatted(userNo) + imageURL;
  }
}
