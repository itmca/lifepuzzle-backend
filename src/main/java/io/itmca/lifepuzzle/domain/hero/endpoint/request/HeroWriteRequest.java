package io.itmca.lifepuzzle.domain.hero.endpoint.request;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.global.constant.ServerConstant;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HeroWriteRequest {

  private Long heroNo;
  private String heroName;
  private String heroNickName;
  private LocalDate birthday;
  private String title;
  private String imageURL;

  public Hero toHero() {
    return toHeroOf(heroNo);
  }

  public Hero toHeroOf(Long heroNo) {
    return toHeroOf(heroNo, null);
  }

  public Hero toHeroOf(MultipartFile photo) {
    return toHeroOf(heroNo, photo);
  }

  public Hero toHeroOf(Long heroNo, MultipartFile photo) {
    var imageURL = StringUtils.hasText(this.imageURL) ? removeFileServerHostInImage() : null;

    return Hero.builder()
        .heroNo(heroNo)
        .name(heroName)
        .nickname(heroNickName)
        .birthday(birthday)
        .title(title)
        .image(imageURL)
        .build();
  }

  private String removeFileServerHostInImage() {
    var fileServerURL = String.format("%s/hero/profile/%d/", ServerConstant.SERVER_HOST, heroNo);
    return imageURL.replace(fileServerURL, "");
  }
}
