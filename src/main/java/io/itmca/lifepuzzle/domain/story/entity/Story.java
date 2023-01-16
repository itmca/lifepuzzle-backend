package io.itmca.lifepuzzle.domain.story.entity;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.story.AgeGroup;
import io.itmca.lifepuzzle.global.constant.ServerConstant;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@Builder
@ToString
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
    private LocalDate date;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public AgeGroup getTag(Hero hero){
        var age = Long.valueOf(date.getYear() - hero.getBirthday().getYear() + 1);
        return AgeGroup.of(age - (age % 10));
    }

    public List<String> getImages(){
        return Arrays.stream(this.imageFiles.split("\\|\\|"))
                .map(file -> String.format("%s/%s/%s", ServerConstant.SERVER_HOST, this.imageFolder, file))
                .toList();
    }

    public List<String> getAudios(){
        return Arrays.stream(this.audioFiles.split("\\|\\|"))
                .map(file -> String.format("%s/%s/%s", ServerConstant.SERVER_HOST, this.audioFolder, file))
                .toList();
    }
}
