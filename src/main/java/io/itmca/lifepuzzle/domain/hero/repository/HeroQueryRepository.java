package io.itmca.lifepuzzle.domain.hero.repository;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HeroQueryRepository extends CrudRepository<HeroUserAuth, Long> {
    HeroUserAuth[] findAllByUserNo(Long userNo);
    Optional<HeroUserAuth> findByUserNoAndHero(Long userNo, Hero hero);
}
