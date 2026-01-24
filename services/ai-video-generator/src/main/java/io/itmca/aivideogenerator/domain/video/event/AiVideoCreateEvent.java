package io.itmca.aivideogenerator.domain.video.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiVideoCreateEvent {
  private Long heroId;
  private Long galleryId;
  private Long drivingVideoId;
}