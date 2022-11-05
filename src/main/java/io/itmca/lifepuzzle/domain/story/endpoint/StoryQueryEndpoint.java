package io.itmca.lifepuzzle.domain.story.endpoint;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("stories")
public class StoryQueryEndpoint {

    @GetMapping()
    public void findStories(){}

    @GetMapping("/{storyKey}")
    public void findSingleStory(){}
}
