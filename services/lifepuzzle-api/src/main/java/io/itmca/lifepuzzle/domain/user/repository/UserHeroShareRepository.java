package io.itmca.lifepuzzle.domain.user.repository;

import io.itmca.lifepuzzle.domain.user.entity.UserHeroShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHeroShareRepository extends JpaRepository<UserHeroShare, String> {
}
