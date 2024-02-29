package io.itmca.lifepuzzle.domain.hero.service;

import static java.util.stream.Collectors.groupingBy;
import static org.springframework.util.CollectionUtils.isEmpty;

import io.itmca.lifepuzzle.domain.hero.endpoint.response.HeroQueryResponse;
import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.hero.repository.HeroRepository;
import io.itmca.lifepuzzle.domain.hero.repository.HeroUserAuthRepository;
import io.itmca.lifepuzzle.domain.story.service.StoryQueryService;
import io.itmca.lifepuzzle.global.exception.HeroNotFoundException;
import java.util.List;
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

  public List<HeroUserAuth> findHeroUserAuthByHeroNo(Long heroNo) {
    var heroUserAuths = this.heroUserAuthRepository.findByHeroNo(heroNo);

    if (isEmpty(heroUserAuths)) {
      throw HeroNotFoundException.byHeroNo(heroNo);
    }

    return heroUserAuths;
  }

  public List<HeroUserAuth> findHeroUserAuthByUserNo(Long userNo) {
    var heroUserAuths = this.heroUserAuthRepository.findByUserNo(userNo);

    if (isEmpty(heroUserAuths)) {
      throw HeroNotFoundException.byUserNo(userNo);
    }

    return heroUserAuths;
  }

  public HeroQueryResponse createHeroQueryResponse(List<HeroUserAuth> heroUserAuths, Long heroNo) {
    var hero = heroUserAuths.stream()
        .findFirst()
        .map(HeroUserAuth::getHero)
        .orElseThrow(() -> HeroNotFoundException.byHeroNo(heroNo));

    int puzzleCnt = storyQueryService.countByHeroNo(heroNo);

    return HeroQueryResponse.from(heroUserAuths, hero, puzzleCnt);
  }

  public List<HeroQueryResponse> createHeroQueryResponseList(List<HeroUserAuth> heroUserAuths) {
    var groupByHeroNo = heroUserAuths.stream()
        .collect(groupingBy(heroUserAuth -> heroUserAuth.getHero().getHeroNo()));

    return groupByHeroNo.entrySet().stream()
        .map(heroGroup -> {
          var heroNo = heroGroup.getKey();
          var userAuths = heroGroup.getValue();

          return createHeroQueryResponse(userAuths, heroNo);
        })
        .toList();
  }
}
