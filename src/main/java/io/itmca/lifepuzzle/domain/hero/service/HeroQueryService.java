package io.itmca.lifepuzzle.domain.hero.service;

import static org.springframework.util.CollectionUtils.isEmpty;

import io.itmca.lifepuzzle.domain.hero.endpoint.response.HeroListQueryResponse;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.HeroQueryResponse;
import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.hero.repository.HeroRepository;
import io.itmca.lifepuzzle.domain.hero.repository.HeroUserAuthRepository;
import io.itmca.lifepuzzle.domain.story.service.StoryQueryService;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.global.exception.HeroNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeroQueryService {
  private final HeroRepository heroRepository;
  private final HeroUserAuthRepository heroUserAuthRepository;
  private final StoryQueryService storyQueryService;

  public Hero findHeroByHeroNo(Long heroNo) {
    return this.heroRepository.findById(heroNo)
        .orElseThrow(() -> HeroNotFoundException.byHeroNo(heroNo));
  }

  public HeroQueryResponse toQueryResponse(Hero hero) {
    int puzzleCnt = storyQueryService.countByHeroNo(hero.getHeroNo());

    return HeroQueryResponse.from(hero, puzzleCnt);
  }

  public HeroListQueryResponse toQueryResponses(User user) {
    var heroUserAuths = user.getHeroUserAuths();
    if (isEmpty(heroUserAuths)) {
      throw HeroNotFoundException.byUserNo(user.getUserNo());
    }

    var heroQueryResponses = heroUserAuths.stream()
        .map(HeroUserAuth::getHero)
        .map(this::toQueryResponse)
        .toList();

    return HeroListQueryResponse.builder()
        .heroes(heroQueryResponses)
        .build();
  }
}
