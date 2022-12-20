package io.itmca.lifepuzzle.domain.story.service;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.story.endpoint.response.StoryTagResponse;
import io.itmca.lifepuzzle.domain.story.entity.Story;
import org.springframework.stereotype.Service;

@Service
public class StoryTagService {

    public StoryTagResponse findTagsByStoryAndHero(Story story, Hero hero){
        var age =  story.getDate().getYear() - hero.getBirthday().getYear() + 1;
        var ageGroup = age - age % 10;





        System.out.println(ageGroup);

        return null;
    }
}
