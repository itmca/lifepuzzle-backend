package io.itmca.lifepuzzle.domain.story.endpoint;

import io.itmca.lifepuzzle.domain.story.endpoint.request.StoryWriteRequest;
import io.itmca.lifepuzzle.domain.story.entity.Story;
import io.itmca.lifepuzzle.domain.story.service.StoryWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StoryWriteEndpoint {
    private final StoryWriteService storyWriteService;

    @PostMapping("story/")
    public void writeStory(@RequestBody StoryWriteRequest storyWriteRequest){
        var dummyUserNo = 1L;
        var photos = "";
        var voice = "";
        var story = StoryWriteRequest.builder()
                .heroNo(storyWriteRequest.getHeroNo())
                .userNo(dummyUserNo)
                .recQuestionNo(storyWriteRequest.getRecQuestionNo())
                .recQuestionModified(storyWriteRequest.getRecQuestionModified())
                .helpQuestionText(storyWriteRequest.getHelpQuestionText())
                .date(storyWriteRequest.getDate())
                .title(storyWriteRequest.getTitle())
                .storyText(storyWriteRequest.getStoryText())
                .imageFiles(photos)
                .audioFiles(voice)
                .build();

        // S3Repository 저장 로직
        System.out.println(storyWriteService.create(Story.from(story)));

    };

}
