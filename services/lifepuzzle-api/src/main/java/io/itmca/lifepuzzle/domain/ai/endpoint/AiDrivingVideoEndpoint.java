package io.itmca.lifepuzzle.domain.ai.endpoint;

import io.itmca.lifepuzzle.domain.ai.endpoint.response.AiDrivingVideoResponse;
import io.itmca.lifepuzzle.domain.ai.endpoint.response.dto.AiDrivingVideoDto;
import io.itmca.lifepuzzle.domain.ai.service.AiDrivingVideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "AI 드라이빙 비디오")
public class AiDrivingVideoEndpoint {
  
  private final AiDrivingVideoService aiDrivingVideoService;
  
  @Operation(summary = "드라이빙 비디오 목록 조회")
  @GetMapping("/v1/ai/driving-videos")
  public ResponseEntity<AiDrivingVideoResponse> getDrivingVideos() {
    var drivingVideos = aiDrivingVideoService.getAllActiveDrivingVideos();
    var drivingVideoDtos = AiDrivingVideoDto.listFrom(drivingVideos);
    var response = AiDrivingVideoResponse.from(drivingVideoDtos);
    
    return ResponseEntity.ok(response);
  }
}