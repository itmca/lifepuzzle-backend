package io.itmca.lifepuzzle.domain.hero.service;

import static org.springframework.util.CollectionUtils.isEmpty;

import io.itmca.lifepuzzle.domain.content.service.StoryQueryService;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.HeroLegacyListQueryResponse;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.HeroLegacyQueryResponse;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.HeroListQueryResponse;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.HeroQueryResponse;
import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.hero.repository.HeroRepository;
import io.itmca.lifepuzzle.domain.hero.repository.HeroUserAuthRepository;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.global.exception.HeroNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeroQueryService {
  private final HeroRepository heroRepository;
  private final StoryQueryService storyQueryService;
  private final HeroUserAuthRepository heroUserAuthRepository;

  public void validateHeroExistOrThrow(Long heroNo) {
    this.heroRepository.findByHeroNoAndDeletedAtIsNull(heroNo)
        .orElseThrow(() -> HeroNotFoundException.byHeroNo(heroNo));
  }

  public Hero findHeroByHeroNo(Long heroNo) {
    return this.heroRepository.findByHeroNoAndDeletedAtIsNull(heroNo)
        .orElseThrow(() -> HeroNotFoundException.byHeroNo(heroNo));
  }

  public HeroLegacyQueryResponse toLegacyQueryResponse(Hero hero, Long userNo) {
    int puzzleCnt = storyQueryService.countByHeroNo(hero.getHeroNo());

    return HeroLegacyQueryResponse.from(hero, userNo, puzzleCnt);
  }

  public HeroLegacyListQueryResponse toLegacyQueryResponses(User user) {
    var heroUserAuths = user.getHeroUserAuths();
    if (isEmpty(heroUserAuths)) {
      throw HeroNotFoundException.byUserNo(user.getId());
    }

    var heroQueryResponses = heroUserAuths.stream()
        .map(HeroUserAuth::getHero)
        .filter(Hero::isActive)
        .map(hero -> toLegacyQueryResponse(hero, user.getId()))
        .toList();

    return new HeroLegacyListQueryResponse(heroQueryResponses);
  }

  public HeroQueryResponse toQueryResponse(Hero hero, Long userNo) {
    return HeroQueryResponse.from(hero, userNo);
  }

  public HeroListQueryResponse toQueryResponses(User user) {
    var heroUserAuths = user.getHeroUserAuths();
    if (isEmpty(heroUserAuths)) {
      throw HeroNotFoundException.byUserNo(user.getId());
    }

    var heroQueryDtos = heroUserAuths.stream()
        .map(HeroUserAuth::getHero)
        .filter(Hero::isActive)
        .map(hero -> toQueryResponse(hero, user.getId()))
        .toList();

    return new HeroListQueryResponse(heroQueryDtos);
  }
}
