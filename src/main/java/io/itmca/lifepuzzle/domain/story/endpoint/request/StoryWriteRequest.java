package io.itmca.lifepuzzle.domain.story.endpoint.request;

import io.itmca.lifepuzzle.domain.story.entity.Story;
import io.itmca.lifepuzzle.global.util.FileUtil;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StoryWriteRequest {
    Long heroNo;
    Long recQuestionNo;
    Boolean recQuestionModified;
    String helpQuestionText;
    LocalDate date;
    String title;
    String storyText;


    public Story toStory(Long userNo, String photoFiles, String audioFiles){
        var storyKey = generatedStoryKey();
        return Story.builder()
                        .storyKey(storyKey)
                        .heroNo(heroNo)
                        .userNo(userNo)
                        .recQuestionNo(recQuestionNo)
                        .usedQuestion(helpQuestionText)
                        .title(title)
                        .content(storyText)
                        .imageFolder(String.format("%s/%s/images", FileUtil.getBaseFolderPath(), storyKey))
                        .imageFiles(photoFiles)
                        .audioFolder(String.format("%s/%s/audio", FileUtil.getBaseFolderPath(), storyKey))
                        .audioFiles(audioFiles)
                        .date(date)
                        .build();
    }

    public String generatedStoryKey() {
       var now = LocalDateTime.now();
       return heroNo.toString() + "-" + now.getHour() + now.getMinute() + now.getSecond();
    }
}
