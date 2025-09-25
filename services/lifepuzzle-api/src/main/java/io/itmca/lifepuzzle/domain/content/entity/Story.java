package io.itmca.lifepuzzle.domain.content.entity;

import static io.itmca.lifepuzzle.global.constants.FileConstant.FILE_NAMES_SEPARATOR;
import static java.util.stream.Collectors.joining;

import io.itmca.lifepuzzle.domain.content.endpoint.request.StoryGalleryWriteRequest;
import io.itmca.lifepuzzle.domain.content.endpoint.request.StoryWriteRequest;
import io.itmca.lifepuzzle.domain.content.type.AgeGroup;
import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.global.constants.ServerConstant;
import io.itmca.lifepuzzle.global.file.CustomFile;
import io.itmca.lifepuzzle.global.file.domain.StoryVoiceFile;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Story {

  @Id
  private String id;
  private Long userId;
  private Long heroId;
  private Long recQuestionId;
  private String usedQuestion;
  @Column(columnDefinition = "tinyint(1) default 0")
  private boolean isQuestionModified;
  private String title;
  private String content;
  private String audioFolder;
  private String audioFiles;
  private String hashtag;

  @OneToMany(mappedBy = "story", orphanRemoval = true)
  private List<Like> likes;

  @OneToMany(mappedBy = "story", orphanRemoval = true)
  private List<StoryGallery> photoMaps;

  private LocalDate date;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(nullable = false)
  @UpdateTimestamp
  private LocalDateTime updatedAt;



  public void setVoice(@Nullable MultipartFile voice) {
    if (voice != null) {
      var storyVoice = new StoryVoiceFile(this, voice);

      this.audioFolder = storyVoice.getBase();
      this.audioFiles = storyVoice.getFileName();
    } else {
      this.audioFolder = "";
      this.audioFiles = "";
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


  public List<String> getAudios() {
    if (!StringUtils.hasText(audioFiles)) {
      return Collections.emptyList();
    }

    return Arrays.stream(this.audioFiles.split("\\|\\|"))
        .map(file -> ServerConstant.S3_SERVER_HOST + this.audioFolder + file)
        .toList();
  }

  public List<String> getAudioNames() {
    if (!StringUtils.hasText(audioFiles)) {
      return Collections.emptyList();
    }

    return Arrays.stream(this.audioFiles.split("\\|\\|")).toList();
  }


  public void updateStoryInfo(StoryWriteRequest storyWriteRequest) {
    this.recQuestionId =
        storyWriteRequest.recQuestionNo() == null ? -1 : storyWriteRequest.recQuestionNo();
    this.isQuestionModified =
        storyWriteRequest.recQuestionModified() == null ? false :
            storyWriteRequest.recQuestionModified();
    this.usedQuestion = storyWriteRequest.helpQuestionText();
    this.date = storyWriteRequest.date();
    this.title = storyWriteRequest.title();
    this.content = storyWriteRequest.storyText();
  }

  public void update(StoryGalleryWriteRequest request) {
    this.date = request.date();
    this.title = request.title();
    this.content = request.content();
  }
}
