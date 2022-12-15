package io.itmca.lifepuzzle.domain.story.endpoint.request;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class StoryWriteRequest {
    Long heroNo;
    Long userNo;
    Long recQuestionNo;
    Boolean recQuestionModified;
    String helpQuestionText;
    LocalDateTime date;
    String title;
    String storyText;
    String imageFiles;
    String audioFiles;
}
