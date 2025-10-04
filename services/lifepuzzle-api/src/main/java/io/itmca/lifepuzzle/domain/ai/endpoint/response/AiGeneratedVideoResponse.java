package io.itmca.lifepuzzle.domain.ai.endpoint.response;

import io.itmca.lifepuzzle.domain.ai.endpoint.response.dto.AiGeneratedVideoDto;
import java.util.List;

public record AiGeneratedVideoResponse(
    List<AiGeneratedVideoDto> generatedVideos
) {
  
  public static AiGeneratedVideoResponse from(List<AiGeneratedVideoDto> generatedVideos) {
    return new AiGeneratedVideoResponse(generatedVideos);
  }
}