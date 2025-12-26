package io.itmca.lifepuzzle.domain.content.entity;

import static io.itmca.lifepuzzle.global.constants.FileConstant.FILE_NAMES_SEPARATOR;
import static java.util.stream.Collectors.joining;

import io.itmca.lifepuzzle.domain.content.endpoint.request.StoryGalleryWriteRequest;
import io.itmca.lifepuzzle.global.constants.ServerConstant;
import io.itmca.lifepuzzle.global.file.CustomFile;
import io.itmca.lifepuzzle.global.file.domain.StoryVoiceFile;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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
  private Long writerId;
  private Long heroId;
  private String content;
  private String audioFolder;
  private String audioFiles;
  private Integer audioDuration;
  private String hashtag;

  @OneToMany(orphanRemoval = true)
  @JoinColumn(name = "content_id", referencedColumnName = "id", insertable = false, updatable = false)
  private List<Like> likes;

  @OneToMany(mappedBy = "story", orphanRemoval = true)
  private List<StoryGallery> photoMaps;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(nullable = false)
  @UpdateTimestamp
  private LocalDateTime updatedAt;



  public void setVoice(@Nullable MultipartFile voice) {
    setVoice(voice, null);
  }

  public void setVoice(@Nullable MultipartFile voice, @Nullable Integer durationSeconds) {
    if (voice != null) {
      var storyVoice = new StoryVoiceFile(this, voice);

      this.audioFolder = storyVoice.getBase();
      this.audioFiles = storyVoice.getFileName();
      this.audioDuration = durationSeconds;
    } else {
      this.audioFolder = "";
      this.audioFiles = "";
      this.audioDuration = null;
    }
  }

  private String getFiles(List<? extends CustomFile> customFiles) {
    return customFiles
        .stream()
        .map(customFile -> customFile.getFileName())
        .collect(joining(FILE_NAMES_SEPARATOR));
  }

  public String getAudioUrl() {
    if (!StringUtils.hasText(audioFiles)) {
      return null;
    }

    String firstAudioFile = this.audioFiles.split("\\|\\|")[0];
    return ServerConstant.S3_SERVER_HOST + this.audioFolder + firstAudioFile;
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

  public void update(StoryGalleryWriteRequest request) {
    this.content = request.content();
  }

  public void updateContent(String content) {
    this.content = content;
  }
}
