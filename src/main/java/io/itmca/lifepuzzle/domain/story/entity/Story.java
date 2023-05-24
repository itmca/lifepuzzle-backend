package io.itmca.lifepuzzle.domain.story.entity;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.story.AgeGroup;
import io.itmca.lifepuzzle.global.constant.ServerConstant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.StringUtils;

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
  @Column(columnDefinition = "tinyint(1) default 0")
  private boolean isQuestionModified;
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

  public AgeGroup getTag(Hero hero) {
    var age = Integer.valueOf(date.getYear() - hero.getBirthday().getYear() + 1);
    return AgeGroup.of(age);
  }

  public List<String> getImages() {
    if (!StringUtils.hasText(imageFiles)) {
      return Collections.emptyList();
    }

    return Arrays.stream(this.imageFiles.split("\\|\\|"))
        .map(file -> String.format("%s/%s/%s", ServerConstant.SERVER_HOST, this.imageFolder, file))
        .toList();
  }

  public List<String> getAudios() {
    if (!StringUtils.hasText(audioFiles)) {
      return Collections.emptyList();
    }

    return Arrays.stream(this.audioFiles.split("\\|\\|"))
        .map(file -> String.format("%s/%s/%s", ServerConstant.SERVER_HOST, this.audioFolder, file))
        .toList();
  }
}
