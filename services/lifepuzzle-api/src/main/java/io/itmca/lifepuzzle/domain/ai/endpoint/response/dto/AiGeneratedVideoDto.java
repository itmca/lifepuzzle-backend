package io.itmca.lifepuzzle.domain.ai.endpoint.response.dto;

import io.itmca.lifepuzzle.domain.ai.entity.AiGeneratedVideo;
import io.itmca.lifepuzzle.domain.ai.type.VideoGenerationStatus;
import java.time.LocalDateTime;
import java.util.List;

public record AiGeneratedVideoDto(
    Long id,
    Long galleryId,
    Long drivingVideoId,
    String videoUrl,
    VideoGenerationStatus status,
    LocalDateTime startedAt,
    LocalDateTime completedAt,
    LocalDateTime createdAt
) {
  
  public static AiGeneratedVideoDto from(AiGeneratedVideo aiGeneratedVideo) {
    return new AiGeneratedVideoDto(
        aiGeneratedVideo.getId(),
        aiGeneratedVideo.getGalleryId(),
        aiGeneratedVideo.getDrivingVideoId(),
        aiGeneratedVideo.getVideoUrl(),
        aiGeneratedVideo.getStatus(),
        aiGeneratedVideo.getStartedAt(),
        aiGeneratedVideo.getCompletedAt(),
        aiGeneratedVideo.getCreatedAt()
    );
  }
  
  public static List<AiGeneratedVideoDto> listFrom(List<AiGeneratedVideo> aiGeneratedVideos) {
    return aiGeneratedVideos.stream()
        .map(AiGeneratedVideoDto::from)
        .toList();
  }
}