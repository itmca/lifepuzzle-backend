package io.itmca.lifepuzzle.domain.user.repository;

import io.itmca.lifepuzzle.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserId(String userId);

    Optional<User> findByEmail(String email);

    Optional<User> findByKakaoId(String kakaoId);

    Optional<User> findByAppleId(String appleId);
}
