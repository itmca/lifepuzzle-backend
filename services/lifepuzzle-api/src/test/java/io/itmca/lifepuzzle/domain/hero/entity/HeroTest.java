package io.itmca.lifepuzzle.domain.hero.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import io.itmca.lifepuzzle.domain.hero.file.HeroProfileImage;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class HeroTest {

  FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
      .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
      .build();

  @DisplayName("이미지 설정 시 새로운 이미지를 전달하면 해당 이미지가 설정된다.")
  @Test
  public void testSettingNewImage() {
    // given
    // TODO: hero 필드들이 null로 초기화 되는 이슈 확인 후 HeroProfileImage 방어 코드 제거 필요
    var hero = fixtureMonkey.giveMeBuilder(Hero.class)
        .set("heroNo", 1L)
        .set("image", "before_update.png")
        .sample();
    var file = fixtureMonkey.giveMeBuilder(MockMultipartFile.class)
        .set("name", "new_image.png")
        .sample();
    var newProfileImage = new HeroProfileImage(hero, file, "postfix");

    // when
    hero.setProfileImage(newProfileImage);

    // then
    assertThat(hero.getImage()).isEqualTo(newProfileImage.getFileName());
  }

  @DisplayName("이미지 설정 시 null을 전달하면 이미지가 초기화된다.")
  @Test
  public void testResetImage() {
    // given
    var hero = fixtureMonkey.giveMeBuilder(Hero.class)
        .set("image", "before_update.png")
        .sample();

    // when
    hero.setProfileImage(null);

    // then
    assertThat(hero.getImage()).isNull();
  }

  @DisplayName("삭제 시 삭제 상태로 변경되고 삭제 시간이 현재 시간으로 설정된다.")
  @Test
  public void testDeleteHero() {
    // given
    var now = LocalDateTime.now();
    var hero = fixtureMonkey.giveMeBuilder(Hero.class)
        .set("heroNo", 1L)
        .set("deletedAt", null)
        .sample();

    // when
    hero.delete();

    // then
    assertThat(hero.isDeleted()).isTrue();
    assertThat(hero.getDeletedAt())
        .isNotNull()
        .isAfter(now);
  }
}
