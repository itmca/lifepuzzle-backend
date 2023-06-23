package io.itmca.lifepuzzle.domain.story.entity;

import static io.itmca.lifepuzzle.global.constant.FileConstant.FILE_NAMES_SEPARATOR;
import static io.itmca.lifepuzzle.global.constant.FileConstant.STORY_BASE_PATH;
import static java.util.stream.Collectors.joining;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.story.AgeGroup;
import io.itmca.lifepuzzle.global.constant.ServerConstant;
import io.itmca.lifepuzzle.global.infra.file.CustomFile;
import io.itmca.lifepuzzle.global.infra.file.ImageCustomFile;
import io.itmca.lifepuzzle.global.infra.file.VoiceCustomFile;
import java.io.File;
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


  public void addImage(List<ImageCustomFile> imageCustomFiles) {
    if (imageCustomFiles.isEmpty()) {
      return;
    }

    this.imageFolder = getFolder(imageCustomFiles.get(0));
    this.imageFiles = getFiles(imageCustomFiles);
  }

  public void addVoice(List<VoiceCustomFile> voiceCustomFiles) {
    if (voiceCustomFiles.isEmpty()) {
      return;
    }

    this.audioFolder = getFolder(voiceCustomFiles.get(0));
    this.audioFiles = getFiles(voiceCustomFiles);
  }

  private String getFolder(CustomFile customFile) {
    return STORY_BASE_PATH + File.separator + this.storyKey + File.separator + customFile.getBase();
  }

  private String getFiles(List<? extends CustomFile> customFiles) {
    return customFiles
        .stream()
        .map(customFile -> customFile.getFileName())
        .collect(joining(FILE_NAMES_SEPARATOR));
  }

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
