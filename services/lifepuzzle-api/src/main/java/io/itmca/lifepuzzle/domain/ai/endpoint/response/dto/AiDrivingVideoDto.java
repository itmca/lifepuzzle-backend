package io.itmca.lifepuzzle.domain.ai.endpoint.response.dto;

import io.itmca.lifepuzzle.domain.ai.entity.AiDrivingVideo;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AiDrivingVideoDto {
  private final Long id;
  private final String name;
  private final String url;
  private final String thumbnailUrl;
  private final String description;
  
  public static AiDrivingVideoDto from(AiDrivingVideo aiDrivingVideo) {
    return new AiDrivingVideoDto(
        aiDrivingVideo.getId(),
        aiDrivingVideo.getName(),
        aiDrivingVideo.getUrl(),
        aiDrivingVideo.getThumbnailUrl(),
        aiDrivingVideo.getDescription()
    );
  }
  
  public static List<AiDrivingVideoDto> listFrom(List<AiDrivingVideo> aiDrivingVideos) {
    return aiDrivingVideos.stream()
        .map(AiDrivingVideoDto::from)
        .toList();
  }
}