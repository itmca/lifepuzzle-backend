package io.itmca.lifepuzzle.domain.story.endpoint;

import io.itmca.lifepuzzle.domain.auth.jwt.AuthPayload;
import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.service.HeroQueryService;
import io.itmca.lifepuzzle.domain.hero.service.HeroValidationService;
import io.itmca.lifepuzzle.domain.story.AgeGroup;
import io.itmca.lifepuzzle.domain.story.endpoint.response.StoryTagResponse;
import io.itmca.lifepuzzle.domain.story.entity.Story;
import io.itmca.lifepuzzle.domain.story.service.StoryQueryService;
import io.itmca.lifepuzzle.domain.story.service.StoryTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Comparator.reverseOrder;

@RestController
@RequiredArgsConstructor
public class StoryQueryEndpoint {
    private final StoryQueryService storyQueryService;
    private final HeroValidationService heroValidationService;
    private final HeroQueryService heroQueryService;
    private final StoryTagService storyTagService;

    //권한 체크는 앞에서 이루어져야 한다는 거시와요
    @GetMapping("/stories")
    public String findStories(@RequestParam("heroNo") Long heroNo, @AuthenticationPrincipal AuthPayload authPayload){
        System.out.println(heroNo + " "  +authPayload.getUserNo());
        heroValidationService.validateUserCanAccessHero(authPayload.getUserNo(), heroNo);

        var hero = heroQueryService.findHeroByUserValidation(heroNo);
        var stories = storyQueryService.findStoriesByHeroId(heroNo);
        var tags = getDistinctTags(stories, hero);

        var tag =  tags.stream().sorted(reverseOrder()).toList();

        System.out.println(AgeGroup.of(10));
        return "hello world2";
    }

    @GetMapping("stories/{storyKey}")
    public void findSingleStory(){}

    private List<StoryTagResponse> getDistinctTags(List<Story> stories, Hero hero){
        var tags = stories.stream()
                .map(story -> storyTagService.findTagsByStoryAndHero(story, hero)).toList();

        System.out.println(tags);
        return null;
    }

}
