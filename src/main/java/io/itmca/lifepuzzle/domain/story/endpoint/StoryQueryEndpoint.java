package io.itmca.lifepuzzle.domain.story.endpoint;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.service.HeroQueryService;
import io.itmca.lifepuzzle.domain.hero.service.HeroValidationService;
import io.itmca.lifepuzzle.domain.story.endpoint.response.StoryTagResponse;
import io.itmca.lifepuzzle.domain.story.entity.Story;
import io.itmca.lifepuzzle.domain.story.service.StoryQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StoryQueryEndpoint {
    private final StoryQueryService storyQueryService;
    private final HeroValidationService heroValidationService;
    private final HeroQueryService heroQueryService;

    //권한 체크는 앞에서 이루어져야 한다는 거시와요
    @GetMapping("stories/")
    public String findStories(@PathParam("heroNo") Long heroNo, @PathParam("userNo") Long userNo){
        heroValidationService.validateUserCanAccessHero(userNo, heroNo);

        var hero = heroQueryService.findHeroByUserValidation(heroNo);
        var stories = storyQueryService.findStoriesByHeroId(heroNo);
        var tags = getDistinctTags(stories, hero);


        return "hello world2";
    }

    @GetMapping("stories/{storyKey}")
    public void findSingleStory(){}

    private List<StoryTagResponse> getDistinctTags(List<Story> stories, Hero hero){
        var tags = stories.stream()
                .map(story -> story.getTag(hero)).toList();

        System.out.println(tags);
        return null;
    }

}
