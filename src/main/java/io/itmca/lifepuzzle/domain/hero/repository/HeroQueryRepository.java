package io.itmca.lifepuzzle.domain.hero.repository;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeroQueryRepository extends JpaRepository<HeroUserAuth, Long> {
    List<HeroUserAuth> findAllByUserNo(Long userNo);

    @Query(value = "SELECT h FROM Hero h WHERE h.heroNo = :heroNo")
    Optional<Hero> findByHeroNoWithJPQL(Long heroNo);
}
