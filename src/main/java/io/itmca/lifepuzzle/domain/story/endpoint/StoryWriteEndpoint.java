package io.itmca.lifepuzzle.domain.story.endpoint;

import io.itmca.lifepuzzle.domain.auth.jwt.AuthPayload;
import io.itmca.lifepuzzle.domain.story.endpoint.request.StoryWriteRequest;
import io.itmca.lifepuzzle.domain.story.service.StoryWriteService;
import io.itmca.lifepuzzle.domain.user.CurrentUser;
import io.itmca.lifepuzzle.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StoryWriteEndpoint {
    private final StoryWriteService storyWriteService;

    @PostMapping("story/")
    public void writeStory(@RequestBody StoryWriteRequest storyWriteRequest,
                           @CurrentUser User user,
                           @AuthenticationPrincipal AuthPayload authPayload){
        authPayload.getUserNo();
        var dummyUserNo = 1L;
        var photos = "";
        var voice = "";
        //Story.from(storyWriteRequest, photos, voice);

        // S3Repository 저장 로직
        //System.out.println(storyWriteService.create(Story.from(story)));

    };

}
