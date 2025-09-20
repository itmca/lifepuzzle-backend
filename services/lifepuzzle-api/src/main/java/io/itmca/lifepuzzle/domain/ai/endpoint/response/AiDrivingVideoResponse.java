package io.itmca.lifepuzzle.domain.ai.endpoint.response;

import io.itmca.lifepuzzle.domain.ai.endpoint.response.dto.AiDrivingVideoDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AiDrivingVideoResponse {
  private final List<AiDrivingVideoDto> drivingVideos;
  
  public static AiDrivingVideoResponse from(List<AiDrivingVideoDto> drivingVideos) {
    return new AiDrivingVideoResponse(drivingVideos);
  }
}