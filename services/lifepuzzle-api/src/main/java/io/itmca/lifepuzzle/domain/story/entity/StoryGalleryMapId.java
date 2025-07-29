package io.itmca.lifepuzzle.domain.story.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class StoryGalleryMapId implements Serializable {
  private String storyId;
  private Long photoId;
}
