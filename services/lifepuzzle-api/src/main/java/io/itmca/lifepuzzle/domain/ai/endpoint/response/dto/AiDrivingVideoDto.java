package io.itmca.lifepuzzle.domain.ai.endpoint.response.dto;

import io.itmca.lifepuzzle.domain.ai.entity.AiDrivingVideo;
import java.util.List;

public record AiDrivingVideoDto(
    Long id,
    String name,
    String url,
    String thumbnailUrl,
    String description
) {
  
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