package io.itmca.lifepuzzle.domain.user.repository;

import io.itmca.lifepuzzle.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public User findByUserId(String userId);
}
