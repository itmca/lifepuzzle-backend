package io.itmca.lifepuzzle.domain.hero.endpoint.response.dto;

import static io.itmca.lifepuzzle.global.constants.FileConstant.USER_PROFILE_IMAGE_BASE_PATH_FORMAT;
import static io.itmca.lifepuzzle.global.constants.ServerConstant.S3_SERVER_HOST;

import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.StringUtils;

@Schema(title = "유저 주인공 권한 조회 DTO")
public record HeroUserAuthQueryDto(
    @Schema(description = "유저키") Long id,
    @Schema(description = "별칭") String nickName,
    @Schema(description = "대표이미지") String imageUrl,
    @Schema(description = "권한") HeroAuthStatus auth
) {

  public static HeroUserAuthQueryDto from(HeroUserAuth heroUserAuth) {
    var user = heroUserAuth.getUser();
    return new HeroUserAuthQueryDto(
        user.getId(),
        user.getNickName(),
        addServerHostInImage(user.getId(), user.getImage()),
        heroUserAuth.getAuth()
    );
  }

  private static String addServerHostInImage(Long userNo, String imageUrl) {
    if (StringUtils.isBlank(imageUrl)) {
      return "";
    }

    return S3_SERVER_HOST + USER_PROFILE_IMAGE_BASE_PATH_FORMAT.formatted(userNo) + imageUrl;
  }
}
