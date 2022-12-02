package io.itmca.lifepuzzle.domain.hero.repository;

import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeroUserAuthQueryRepository extends JpaRepository<HeroUserAuth, Long> {
    @Query(value = "SELECT auth FROM HeroUserAuth auth WHERE auth.userNo = :userNo AND auth.hero.heroNo = :heroNo")
    List<HeroUserAuth> findByUserNoAndHeroNo(@Param("userNo")Long userNo, @Param("heroNo")Long heroNo);
}
