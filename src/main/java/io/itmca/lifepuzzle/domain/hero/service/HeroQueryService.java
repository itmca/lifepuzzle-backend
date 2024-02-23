package io.itmca.lifepuzzle.domain.hero.service;

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
        .orElseThrow(() -> new HeroNotFoundException(heroNo));
  }

  public List<HeroUserAuth> findHeroUserAuthByHeroNo(Long heroNo) {
    var heroUserAuths = this.heroUserAuthRepository.findByHeroNo(heroNo);

    if (isEmpty(heroUserAuths)) {
      throw new HeroNotFoundException(heroNo);
    }

    return heroUserAuths;
  }

  public List<Hero> findHeroesByUserNo(Long userNo) {
    var heroUserAuths = this.heroUserAuthRepository.findAllByUser_UserNo(userNo);

    return heroUserAuths.stream()
        .map(HeroUserAuth::getHero)
        .toList();
  }

  public HeroQueryResponse createHeroQueryResponse(List<HeroUserAuth> heroUserAuths) {
    int puzzleCnt = storyQueryService.countByHeroNo(heroUserAuths.get(0).getHero().getHeroNo());

    return HeroQueryResponse.from(heroUserAuths, puzzleCnt);
  }
}
