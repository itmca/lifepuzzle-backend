package io.itmca.lifepuzzle.domain.story.endpoint;

import io.itmca.lifepuzzle.domain.auth.jwt.AuthPayload;
import io.itmca.lifepuzzle.domain.hero.service.HeroQueryService;
import io.itmca.lifepuzzle.domain.hero.service.HeroValidationService;
import io.itmca.lifepuzzle.domain.story.endpoint.response.StoryQueryResponse;
import io.itmca.lifepuzzle.domain.story.service.StoryQueryService;
import io.itmca.lifepuzzle.domain.story.service.StoryTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequiredArgsConstructor
public class StoryQueryEndpoint {
    private final StoryQueryService storyQueryService;
    private final StoryTagService storyTagService;
    private final HeroValidationService heroValidationService;
    private final HeroQueryService heroQueryService;

    @GetMapping("/stories")
    public StoryQueryResponse findStories(@RequestParam("heroNo") Long heroNo,
                                          @AuthenticationPrincipal AuthPayload authPayload){
        heroValidationService.validateUserCanAccessHero(authPayload.getUserNo(), heroNo);

        var hero = heroQueryService.findHeroByHeroNo(heroNo);
        var stories = storyQueryService.findStoriesByHeroId(heroNo);
        var tags = storyTagService.getDistinctTags(stories, hero);

        return StoryQueryResponse.from(stories, tags);
    }

    @GetMapping("/stories/{storyKey}")
    public StoryQueryResponse findSingleStory(@PathVariable("storyKey") String storyKey,
                                @AuthenticationPrincipal AuthPayload authPayload){
        var story = storyQueryService.findById(storyKey);
        var hero = heroQueryService.findHeroByHeroNo(story.getHeroNo());

        heroValidationService.validateUserCanAccessHero(authPayload.getUserNo(), hero.getHeroNo());

        var tags = storyTagService.getDistinctTags(Arrays.asList(story), hero);

        return StoryQueryResponse.from(Arrays.asList(story), tags);
    }

}
