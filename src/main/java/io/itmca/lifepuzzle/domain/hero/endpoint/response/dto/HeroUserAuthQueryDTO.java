package io.itmca.lifepuzzle.domain.hero.endpoint.response.dto;

import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

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
        .imageURL(user.getImage())
        .auth(heroUserAuth.getAuth())
        .build();
  }
}
