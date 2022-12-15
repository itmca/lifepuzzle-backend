package io.itmca.lifepuzzle.domain.user.repository;

import io.itmca.lifepuzzle.domain.user.entity.UserEmailValidation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEmailValidationRepository extends JpaRepository<UserEmailValidation, Long> {

    Optional<UserEmailValidation> findFirstByEmailOrderBySeqDesc(String email);
}
