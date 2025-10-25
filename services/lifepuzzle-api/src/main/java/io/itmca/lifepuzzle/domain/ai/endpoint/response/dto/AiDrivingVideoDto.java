package io.itmca.lifepuzzle.domain.ai.endpoint.response.dto;

import io.itmca.lifepuzzle.domain.ai.entity.AiDrivingVideo;
import io.itmca.lifepuzzle.global.constants.FileConstant;
import io.itmca.lifepuzzle.global.constants.ServerConstant;
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
        ServerConstant.S3_SERVER_HOST + FileConstant.AI_DRIVING_VIDEO_BASE_PATH + aiDrivingVideo.getUrl(),
        ServerConstant.S3_SERVER_HOST + FileConstant.AI_DRIVING_THUMBNAIL_BASE_PATH + aiDrivingVideo.getThumbnailUrl(),
        aiDrivingVideo.getDescription()
    );
  }
  
  public static List<AiDrivingVideoDto> listFrom(List<AiDrivingVideo> aiDrivingVideos) {
    return aiDrivingVideos.stream()
        .map(AiDrivingVideoDto::from)
        .toList();
  }
}