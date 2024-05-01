package io.itmca.lifepuzzle.domain.story.endpoint;

import static io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus.ADMIN;
import static io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus.COMMENTER;
import static io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus.OWNER;
import static io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus.VIEWER;
import static io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus.WRITER;

import io.itmca.lifepuzzle.domain.auth.jwt.AuthPayload;
import io.itmca.lifepuzzle.domain.hero.service.HeroQueryService;
import io.itmca.lifepuzzle.domain.hero.service.HeroValidationService;
import io.itmca.lifepuzzle.domain.story.endpoint.response.StoryQueryResponse;
import io.itmca.lifepuzzle.domain.story.endpoint.response.dto.StoryDTO;
import io.itmca.lifepuzzle.domain.story.service.StoryQueryService;
import io.itmca.lifepuzzle.domain.story.service.StoryTagService;
import io.itmca.lifepuzzle.global.aop.AuthCheck;
import io.itmca.lifepuzzle.global.aop.HeroNo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "스토리 조회")
public class StoryQueryEndpoint {
  private final StoryQueryService storyQueryService;
  private final StoryTagService storyTagService;
  private final HeroValidationService heroValidationService;
  private final HeroQueryService heroQueryService;

  @AuthCheck(auths = { VIEWER, COMMENTER, WRITER, ADMIN, OWNER })
  @GetMapping("/stories")
  @Operation(summary = "스토리 전체 목록 조회")
  public StoryQueryResponse findStories(@RequestParam("heroNo") @HeroNo Long heroNo,
                                        @AuthenticationPrincipal AuthPayload authPayload) {
    // [Debugging]
    // heroNo 가 -1일 경우 무조건 exception이 떨어지는 문제. heroNo의 -1은 default인데.... 다른 부분들도 전부 처리해야할듯
    if (heroNo == -1) {
      return StoryQueryResponse.getEmptyResponse();
    }
    heroValidationService.validateUserCanAccessHero(authPayload.getUserNo(), heroNo);

    var hero = heroQueryService.findHeroByHeroNo(heroNo);
    var stories = storyQueryService.findStoriesByHeroId(heroNo);
    var tags = storyTagService.getDistinctTags(stories, hero);

    return StoryQueryResponse.from(stories, hero, tags);
  }

  @Operation(summary = "스토리 조회")
  @GetMapping("/stories/{storyKey}")
  public StoryDTO findSingleStory(@PathVariable("storyKey") String storyKey,
                                  @AuthenticationPrincipal AuthPayload authPayload) {
    var story = storyQueryService.findById(storyKey);
    var hero = heroQueryService.findHeroByHeroNo(story.getHeroNo());

    heroValidationService.validateUserCanAccessHero(authPayload.getUserNo(), hero.getHeroNo());

    return StoryDTO.from(story, hero);
  }

}
