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
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeroQueryService {
  private final HeroRepository heroRepository;
  private final StoryQueryService storyQueryService;
  private final HeroUserAuthRepository heroUserAuthRepository;

  public Hero findHeroByHeroNo(Long heroNo) {
    return this.heroRepository.findById(heroNo)
        .orElseThrow(() -> HeroNotFoundException.byHeroNo(heroNo));
  }

  @Deprecated
  public List<Hero> findHeroesByUserNo(Long userNo) {
    var heroUserAuths = this.heroUserAuthRepository.findAllByUser_Id(userNo);

    return heroUserAuths.stream()
        .map(HeroUserAuth::getHero)
        .toList();
  }

  public HeroQueryResponse toQueryResponse(Hero hero, Long userNo) {
    int puzzleCnt = storyQueryService.countByHeroNo(hero.getHeroNo());

    return HeroQueryResponse.from(hero, userNo, puzzleCnt);
  }

  public HeroListQueryResponse toQueryResponses(User user) {
    var heroUserAuths = user.getHeroUserAuths();
    if (isEmpty(heroUserAuths)) {
      throw HeroNotFoundException.byUserNo(user.getId());
    }

    var heroQueryResponses = heroUserAuths.stream()
        .map(HeroUserAuth::getHero)
        .map(hero -> toQueryResponse(hero, user.getId()))
        .toList();

    return HeroListQueryResponse.builder()
        .heroes(heroQueryResponses)
        .build();
  }
}
