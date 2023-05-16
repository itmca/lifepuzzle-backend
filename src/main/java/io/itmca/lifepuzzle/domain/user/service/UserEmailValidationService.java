package io.itmca.lifepuzzle.domain.user.service;

import io.itmca.lifepuzzle.domain.user.entity.UserEmailValidation;
import io.itmca.lifepuzzle.domain.user.repository.UserEmailValidationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEmailValidationService {

  private final UserEmailValidationRepository userEmailValidationRepository;

  public UserEmailValidation create(UserEmailValidation userEmailValidation) {
    return userEmailValidationRepository.save(userEmailValidation);
  }

  public UserEmailValidation findRecentOneByEmail(String email) {
    return userEmailValidationRepository.findFirstByEmailOrderBySeqDesc(email)
        .orElseThrow(() -> new IllegalArgumentException("no such data"));
  }
}
