package io.itmca.lifepuzzle.domain.hero.service;

import static io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus.OWNER;
import static io.itmca.lifepuzzle.global.constant.FileConstant.HERO_PROFILE_IMAGE_BASE_PATH_FORMAT;

import io.itmca.lifepuzzle.domain.hero.endpoint.request.HeroWriteRequest;
import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.file.HeroProfileImage;
import io.itmca.lifepuzzle.domain.hero.repository.HeroRepository;
import io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.global.exception.HeroNotFoundException;
import io.itmca.lifepuzzle.global.infra.file.service.S3UploadService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class HeroWriteService {
  private final HeroRepository heroRepository;
  private final S3UploadService s3UploadService;
  private final HeroUserAuthWriteService heroUserAuthWriteService;

  public Hero create(HeroWriteRequest request, User user, @Nullable MultipartFile profile) {
    var hero = request.toHero();

    var savedHero = heroRepository.save(hero);
    postCreateAction(user, profile, hero, savedHero);

    return savedHero;
  }

  private void postCreateAction(User user, MultipartFile profile, Hero hero, Hero savedHero) {
    var uploadedProfileImage = uploadProfileImage(profile, hero);
    hero.setProfileImage(uploadedProfileImage.orElse(null));

    heroUserAuthWriteService.authorize(user, savedHero, OWNER);
  }

  private Optional<HeroProfileImage> uploadProfileImage(@Nullable MultipartFile profile,
                                                        Hero hero) {
    if (profile == null) {
      return Optional.empty();
    }

    // TODO : resize 실패가 자주 발생해 추석 기간 동안 resize 제거. 추석 이후 원인 파악하여 resize 복구 예정
    var heroProfileImage = new HeroProfileImage(
        hero,
        profile,
        String.valueOf(System.currentTimeMillis())
    );
    s3UploadService.upload(heroProfileImage);

    return Optional.of(heroProfileImage);
  }

  public Hero createDefaultHero(User user) {
    var created = heroRepository.save(Hero.defaultHero());

    heroUserAuthWriteService.authorize(user, created, HeroAuthStatus.OWNER);

    return created;
  }

  public Hero update(Long heroNo, HeroWriteRequest heroWriteRequest, MultipartFile profile) {
    var hero = heroRepository.findById(heroNo)
        .orElseThrow(() -> HeroNotFoundException.byHeroNo(heroNo));

    hero.setTitle(heroWriteRequest.getTitle());
    hero.setName(heroWriteRequest.getHeroName());
    hero.setNickname(heroWriteRequest.getHeroNickName());
    hero.setBirthday(heroWriteRequest.getBirthday());
    hero.setLunar(heroWriteRequest.isLunar());

    var isProfileImageUpdate = heroWriteRequest.isProfileImageUpdate();

    if (isProfileImageUpdate) {
      if (profile == null) {
        s3UploadService.delete(
            HERO_PROFILE_IMAGE_BASE_PATH_FORMAT.formatted(heroNo) + hero.getImage());
      }

      var uploadedProfileImage = uploadProfileImage(profile, hero);
      hero.setProfileImage(uploadedProfileImage.orElse(null));
    }

    return heroRepository.save(hero);
  }

  public Hero updateProfile(Long heroNo, MultipartFile profile) {
    var hero = heroRepository.findById(heroNo)
        .orElseThrow(() -> HeroNotFoundException.byHeroNo(heroNo));

    var uploadedProfileImage = uploadProfileImage(profile, hero);
    hero.setProfileImage(uploadedProfileImage.orElse(null));

    return heroRepository.save(hero);
  }

  public void remove(Long heroNo) {
    heroRepository.deleteById(heroNo);
  }
}
