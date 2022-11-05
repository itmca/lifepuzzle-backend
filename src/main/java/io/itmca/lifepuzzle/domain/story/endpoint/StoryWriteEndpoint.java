package io.itmca.lifepuzzle.domain.story.endpoint;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller("story")
public class StoryWriteEndpoint {

    @PostMapping()
    public void writeStory(){};

}
