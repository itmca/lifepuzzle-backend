package io.itmca.lifepuzzle.domain.ai.endpoint.response;

import io.itmca.lifepuzzle.domain.ai.endpoint.response.dto.AiDrivingVideoDto;
import java.util.List;

public record AiDrivingVideoResponse(
    List<AiDrivingVideoDto> drivingVideos
) {
  
  public static AiDrivingVideoResponse from(List<AiDrivingVideoDto> drivingVideos) {
    return new AiDrivingVideoResponse(drivingVideos);
  }
}