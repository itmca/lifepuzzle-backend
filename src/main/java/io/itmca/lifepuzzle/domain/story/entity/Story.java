package io.itmca.lifepuzzle.domain.story.entity;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.story.endpoint.request.StoryWriteRequest;
import io.itmca.lifepuzzle.domain.story.endpoint.response.StoryTagResponse;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Story {

    @Id
    private String storyKey;
    private Long userNo;
    private Long heroNo;
    private Long recQuestionNo;
    private String usedQuestion;
    private Boolean isQuestionModified;
    private String title;
    private String content;
    private String imageFolder;
    private String imageFiles;
    private String audioFolder;
    private String audioFiles;
    private String hashtag;
    private LocalDateTime date;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime updatedAt;

    public static Story from(StoryWriteRequest storyWriteRequest){
        var baseFolderPath = "";
        var storyKey = "" + storyWriteRequest.getStoryText();

        return Story.builder()
                .storyKey(storyKey)
                .heroNo(storyWriteRequest.getHeroNo())
                .userNo(storyWriteRequest.getUserNo())
                .recQuestionNo(storyWriteRequest.getRecQuestionNo())
                .usedQuestion(storyWriteRequest.getHelpQuestionText())
                .title(storyWriteRequest.getTitle())
                .content(storyWriteRequest.getStoryText())
                .imageFolder(String.format("%s/$s/images", baseFolderPath, storyKey))
                .imageFiles(storyWriteRequest.getImageFiles())
                .audioFolder(String.format("%s/$s/audio", baseFolderPath, storyKey))
                .audioFiles(storyWriteRequest.getAudioFiles())
                .date(storyWriteRequest.getDate())
                .build();
    }

    public StoryTagResponse getTag(Hero hero) {
        var birthday = hero.getBirthday();
        var age =  date.getYear() - birthday.getYear() + 1;
        var ageGroup = age - age % 10;

        return StoryTagResponse.builder()
                .key(Integer.toString(ageGroup))
                .displayName(ageGroup > 0 ? String.format("%l대",ageGroup) : "10대 미만")
                .priority(ageGroup)
                .build();
    }
}
