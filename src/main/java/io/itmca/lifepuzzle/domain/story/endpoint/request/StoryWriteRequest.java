package io.itmca.lifepuzzle.domain.story.endpoint.request;

import io.itmca.lifepuzzle.domain.story.entity.Story;
import io.itmca.lifepuzzle.global.constant.FileConstant;
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


    public Story toStoryOf(Long userNo, String photoFiles, String audioFiles){
        var storyKey = generatedStoryKey();
        System.out.println(storyKey);
        return Story.builder()
                        .storyKey(storyKey)
                        .heroNo(heroNo)
                        .userNo(userNo)
                        .recQuestionNo(recQuestionNo)
                        .usedQuestion(helpQuestionText)
                        .title(title)
                        .content(storyText)
                        .imageFolder(String.format("%s/$s/images", FileConstant.BASE_FOLDER_PATH, storyKey))
                        .imageFiles(photoFiles)
                        .audioFolder(String.format("%s/$s/audio", FileConstant.BASE_FOLDER_PATH, storyKey))
                        .audioFiles(audioFiles)
                        .date(date)
                        .build();
    }

    public String generatedStoryKey() {
       var now = LocalDateTime.now();
       return String.format("%s-%02d%02d%02d",
               heroNo.toString(),
               now.getHour(),
               now.getMinute(),
               now.getSecond());
    }
}
