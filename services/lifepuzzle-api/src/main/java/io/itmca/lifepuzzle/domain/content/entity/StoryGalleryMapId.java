package io.itmca.lifepuzzle.domain.content.entity;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class StoryGalleryMapId implements Serializable {
  private String storyId;
  private Long photoId;
}
