package io.itmca.lifepuzzle.domain.story.endpoint;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("story")
public class StoryWriteEndpoint {

    @PostMapping("/")
    public void writeStory(){};

}
