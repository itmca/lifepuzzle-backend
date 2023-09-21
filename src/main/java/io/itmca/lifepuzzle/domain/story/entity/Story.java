package io.itmca.lifepuzzle.domain.story.entity;

import static io.itmca.lifepuzzle.global.constant.FileConstant.FILE_NAMES_SEPARATOR;
import static java.util.stream.Collectors.joining;
import static org.springframework.util.CollectionUtils.isEmpty;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.story.AgeGroup;
import io.itmca.lifepuzzle.domain.story.endpoint.request.StoryWriteRequest;
import io.itmca.lifepuzzle.domain.story.file.StoryFile;
import io.itmca.lifepuzzle.global.constant.ServerConstant;
import io.itmca.lifepuzzle.global.infra.file.CustomFile;
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
  private String videoFolder;
  private String videoFiles;
  private String hashtag;
  private LocalDate date;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(nullable = false)
  @UpdateTimestamp
  private LocalDateTime updatedAt;


  public void addStoryFile(StoryFile storyFile) {
    if (!isEmpty(storyFile.images())) {
      var storyImages = storyFile.images();

      this.imageFolder = storyImages.get(0).getBase();
      this.imageFiles = getFiles(storyImages);
    } else {
      this.imageFolder = "";
      this.imageFiles = "";
    }

    if (!isEmpty(storyFile.voices())) {
      var storyVoices = storyFile.voices();

      this.audioFolder = storyVoices.get(0).getBase();
      this.audioFiles = getFiles(storyVoices);
    } else {
      this.audioFolder = "";
      this.audioFiles = "";
    }

    if (!isEmpty(storyFile.videos())) {
      var storyVideos = storyFile.videos();

      this.videoFolder = storyVideos.get(0).getBase();
      this.videoFiles = getFiles(storyVideos);
    } else {
      this.videoFolder = "";
      this.videoFiles = "";
    }
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

  public List<String> getImageNames() {
    if (!StringUtils.hasText(imageFiles)) {
      return Collections.emptyList();
    }

    return Arrays.stream(this.imageFiles.split("\\|\\|")).toList();
  }

  public List<String> getAudios() {
    if (!StringUtils.hasText(audioFiles)) {
      return Collections.emptyList();
    }

    return Arrays.stream(this.audioFiles.split("\\|\\|"))
        .map(file -> String.format("%s/%s/%s", ServerConstant.SERVER_HOST, this.audioFolder, file))
        .toList();
  }

  public List<String> getAudioNames() {
    if (!StringUtils.hasText(audioFiles)) {
      return Collections.emptyList();
    }

    return Arrays.stream(this.audioFiles.split("\\|\\|")).toList();
  }

  public List<String> getVideos() {
    if (!StringUtils.hasText(videoFiles)) {
      return Collections.emptyList();
    }

    return Arrays.stream(this.videoFiles.split("\\|\\|"))
        .map(file -> String.format("%s/%s/%s", ServerConstant.SERVER_HOST, this.videoFolder, file))
        .toList();
  }

  public List<String> getVideoNames() {
    if (!StringUtils.hasText(videoFiles)) {
      return Collections.emptyList();
    }

    return Arrays.stream(this.videoFiles.split("\\|\\|")).toList();
  }

  public void updateStoryInfo(StoryWriteRequest storyWriteRequest) {
    this.recQuestionNo =
        storyWriteRequest.getRecQuestionNo() == null ? -1 : storyWriteRequest.getRecQuestionNo();
    this.isQuestionModified =
        storyWriteRequest.getRecQuestionModified() == null ? false :
            storyWriteRequest.getRecQuestionModified();
    this.usedQuestion = storyWriteRequest.getHelpQuestionText();
    this.date = storyWriteRequest.getDate();
    this.title = storyWriteRequest.getTitle();
    this.content = storyWriteRequest.getStoryText();
  }
}
