package io.itmca.lifepuzzle.domain.hero.repository;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeroRepository extends JpaRepository<Hero, Long> {

}
