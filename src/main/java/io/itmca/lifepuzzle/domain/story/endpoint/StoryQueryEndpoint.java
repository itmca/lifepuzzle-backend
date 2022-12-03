package io.itmca.lifepuzzle.domain.story.endpoint;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("stories")
public class StoryQueryEndpoint {

    @GetMapping("/")
    public String findStories(){
        return "hello world2";
    }

    @GetMapping("/{storyKey}")
    public void findSingleStory(){}
}
