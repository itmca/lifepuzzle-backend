package io.itmca.lifepuzzle.domain.user.repository;

import io.itmca.lifepuzzle.domain.user.entity.UserEmailValidation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEmailValidationRepository extends JpaRepository<UserEmailValidation, Long> {

  Optional<UserEmailValidation> findFirstByEmailOrderBySeqDesc(String email);
}
