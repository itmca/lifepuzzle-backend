package io.itmca.lifepuzzle.domain.user.service;

import static io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus.ADMIN;
import static io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus.OWNER;
import static io.itmca.lifepuzzle.global.constant.FileConstant.USER_PROFILE_IMAGE_BASE_PATH_FORMAT;

import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus;
import io.itmca.lifepuzzle.domain.user.endpoint.request.UserUpdateRequest;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.domain.user.entity.UserHeroShare;
import io.itmca.lifepuzzle.domain.user.file.UserProfileImage;
import io.itmca.lifepuzzle.domain.user.repository.UserHeroShareRepository;
import io.itmca.lifepuzzle.domain.user.repository.UserRepository;
import io.itmca.lifepuzzle.global.constant.ServerConstant;
import io.itmca.lifepuzzle.global.exception.UserNotAccessibleToHeroException;
import io.itmca.lifepuzzle.global.exception.UserNotShareHeroAuthException;
import io.itmca.lifepuzzle.global.infra.file.service.S3UploadService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserWriteService {

  private final UserRepository userRepository;
  private final S3UploadService s3UploadService;
  private final UserHeroShareRepository userHeroShareRepository;

  public User save(User user) {
    return userRepository.save(user);
  }

  @Transactional
  public void updateUserPassword(User user, String password) {
    user.hashCredential(password);
  }

  public void deleteByUserNo(Long userNo) {
    userRepository.deleteById(userNo);
  }

  @Transactional
  public void changeRecentHeroNo(User user, Long heroNo) {
    user.changeRecentHeroNo(heroNo);
  }

  @Transactional
  public String createHeroShareLink(User user, long heroNo, HeroAuthStatus shareAuth) {
    HeroUserAuth heroUserAuth = user.getHeroUserAuths()
        .stream().filter(h -> heroNo == h.getHero().getHeroNo()).findFirst().orElseThrow(
            () -> new UserNotAccessibleToHeroException("user can not access hero")
        );
    HeroAuthStatus userAuth = heroUserAuth.getAuth();

    if (userAuth != OWNER && userAuth != ADMIN) {
      throw new UserNotAccessibleToHeroException("user can not access hero");
    }

    if (userAuth.getPriority() <= shareAuth.priority) {
      throw new UserNotShareHeroAuthException("user can not share hero auth");
    }

    long userNo = user.getId();
    String shareKey = createHeroAuthShareKey(userNo);
    LocalDateTime now = LocalDateTime.now();

    userHeroShareRepository.save(UserHeroShare.builder()
        .id(shareKey)
        .sharerUserId(userNo)
        .heroId(heroNo)
        .auth(shareAuth)
        .createdAt(now)
        .expiredAt(now.plusDays(7))
        .build()
    );

    return ServerConstant.DEEP_LINK_SERVER_HOST + "share/hero?shareKey=" + shareKey;
  }

  private String createHeroAuthShareKey(long userNo) {
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMdd"))
        + "-" + userNo
        + "-" + RandomStringUtils.randomAlphanumeric(4);
  }

  public User update(User user, UserUpdateRequest userUpdateRequest, MultipartFile photo) {
    var isProfileImageUpdate = userUpdateRequest.isProfileImageUpdate();
    if (isProfileImageUpdate) {
      if (photo == null) {
        s3UploadService.delete(
            USER_PROFILE_IMAGE_BASE_PATH_FORMAT.formatted(user.getId().toString())
                + user.getImage());
      }

      val userProfileImage = uploadProfileImage(photo, user).orElse(null);

      user.setProfileImage(userProfileImage);
    }

    user.updateUserInfo(userUpdateRequest);

    return userRepository.save(user);
  }

  private Optional<UserProfileImage> uploadProfileImage(@Nullable MultipartFile profile,
                                                        User user) {
    if (profile == null) {
      return Optional.empty();
    }

    // TODO : resize 실패가 자주 발생해 추석 기간 동안 resize 제거. 추석 이후 원인 파악하여 resize 복구 예정
    var heroProfileImage = new UserProfileImage(
        user,
        profile,
        String.valueOf(System.currentTimeMillis())
    );
    s3UploadService.upload(heroProfileImage);

    return Optional.of(heroProfileImage);
  }
}
