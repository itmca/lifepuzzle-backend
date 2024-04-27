package io.itmca.lifepuzzle.domain.user.service;

import static io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus.ADMIN;
import static io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus.OWNER;

import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.domain.user.entity.UserHeroShare;
import io.itmca.lifepuzzle.domain.user.repository.UserHeroShareRepository;
import io.itmca.lifepuzzle.domain.user.repository.UserRepository;
import io.itmca.lifepuzzle.global.constant.ServerConstant;
import io.itmca.lifepuzzle.global.exception.UserNotAccessibleToHeroException;
import io.itmca.lifepuzzle.global.exception.UserNotShareHeroAuthException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserWriteService {

  private final UserRepository userRepository;
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

    long userNo = user.getUserNo();
    String shareKey = createHeroAuthShareKey(userNo);
    LocalDateTime now = LocalDateTime.now();

    userHeroShareRepository.save(UserHeroShare.builder()
        .id(shareKey)
        .ownerNo(userNo)
        .heroNo(heroNo)
        .auth(shareAuth)
        .createdAt(now)
        .expiredAt(now.plusDays(7))
        .build()
    );

    return ServerConstant.DEEP_LINK_SERVER_HOST + "share/hero/" + shareKey;
  }

  private String createHeroAuthShareKey(long userNo) {
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMdd"))
        + "-" + userNo
        + "-" + RandomStringUtils.randomAlphanumeric(4);
  }
}
