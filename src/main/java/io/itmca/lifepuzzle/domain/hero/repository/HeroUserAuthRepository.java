package io.itmca.lifepuzzle.domain.hero.repository;

import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HeroUserAuthRepository extends JpaRepository<HeroUserAuth, Long> {

  @Query("SELECT auth FROM HeroUserAuth auth "
      + "JOIN FETCH auth.hero "
      + "JOIN FETCH auth.user "
      + "WHERE auth.hero.heroNo IN ("
      + "    SELECT auth2.hero.heroNo FROM HeroUserAuth auth2 WHERE auth2.user.userNo = :userNo"
      + ")")
  List<HeroUserAuth> findByUserNo(@Param("userNo") Long userNo);

  @Query("SELECT auth FROM HeroUserAuth auth "
      + "WHERE auth.user.userNo = :userNo AND auth.hero.heroNo = :heroNo")
  List<HeroUserAuth> findByUserNoAndHeroNo(@Param("userNo") Long userNo,
                                           @Param("heroNo") Long heroNo);

  @Query("SELECT auth FROM HeroUserAuth auth "
      + "JOIN FETCH auth.hero "
      + "JOIN FETCH auth.user "
      + "WHERE auth.hero.heroNo = :heroNo")
  List<HeroUserAuth> findByHeroNo(@Param("heroNo") Long heroNo);
}
