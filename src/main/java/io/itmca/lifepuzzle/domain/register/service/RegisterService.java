package io.itmca.lifepuzzle.domain.register.service;

import io.itmca.lifepuzzle.domain.register.PasswordVerification;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.domain.user.service.UserQueryService;
import io.itmca.lifepuzzle.domain.user.service.UserWriteService;
import io.itmca.lifepuzzle.global.exception.PasswordMismatchException;
import io.itmca.lifepuzzle.global.exception.UserAlreadyExistsException;
import io.itmca.lifepuzzle.global.exception.handler.NotFoundException;
import io.itmca.lifepuzzle.global.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class RegisterService {

  private final UserWriteService userWriteService;
  private final RegisterPostActionService registerPostActionService;
  private final NicknameProvideService nicknameProvideService;
  private final UserQueryService userQueryService;

  @Transactional
  public void register(User user, String shareKey) {
    if (isAlreadyExist(user)) {
      throw new UserAlreadyExistsException(user.getLoginId());
    }

    var registeredUser = this.registerInternally(user);

    registerPostActionService.doAfterRegisterActions(registeredUser, shareKey);
  }

  private boolean isAlreadyExist(User user) {
    try {
      userQueryService.findByLoginId(user.getLoginId());
    } catch (NotFoundException e) {
      return false;
    }
    return true;
  }

  private User registerInternally(User user) {
    this.setSaltAndEncodedPasswordToUser(user);

    if (!StringUtils.hasText(user.getNickName())) {
      user.setNickname(nicknameProvideService.getRandomNickname(user.getLoginId()));
    }

    return this.userWriteService.save(user);
  }

  private void setSaltAndEncodedPasswordToUser(User user) {
    var originPassword = user.getPassword();
    user.hashCredential(originPassword);

    if (!isPasswordCorrectlyGenerated(user, originPassword)) {
      throw new PasswordMismatchException();
    }
  }

  private Boolean isPasswordCorrectlyGenerated(User user, String originPassword) {
    return PasswordUtil.matches(PasswordVerification.builder()
        .plainPassword(originPassword)
        .salt(user.getSalt())
        .hashedPassword(user.getPassword())
        .build());
  }
}
