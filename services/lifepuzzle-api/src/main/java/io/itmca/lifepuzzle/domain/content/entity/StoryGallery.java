package io.itmca.lifepuzzle.domain.content.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

@Table(name = "story_gallery")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@IdClass(StoryGalleryId.class)
public class StoryGallery {
  @Id
  @Column(name = "story_id", nullable = false)
  private String storyId;
  @Id
  @Column(name = "gallery_id", nullable = false)
  private Long galleryId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "story_id", referencedColumnName = "id", insertable = false, updatable = false)
  private Story story;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "gallery_id", referencedColumnName = "id", insertable = false, updatable = false)
  private Gallery gallery;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;

  public static StoryGallery create(Story story, Long galleryId) {
    return StoryGallery.builder()
        .storyId(story.getId())
        .galleryId(galleryId)
        .build();
  }
}

