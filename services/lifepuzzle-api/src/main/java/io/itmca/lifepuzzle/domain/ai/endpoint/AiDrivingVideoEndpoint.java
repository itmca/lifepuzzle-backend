package io.itmca.lifepuzzle.domain.ai.endpoint;

import io.itmca.lifepuzzle.domain.ai.endpoint.request.AiPhotoGenerateRequest;
import io.itmca.lifepuzzle.domain.ai.endpoint.response.AiDrivingVideoResponse;
import io.itmca.lifepuzzle.domain.ai.endpoint.response.AiGeneratedVideoResponse;
import io.itmca.lifepuzzle.domain.ai.endpoint.response.dto.AiDrivingVideoDto;
import io.itmca.lifepuzzle.domain.ai.endpoint.response.dto.AiGeneratedVideoDto;
import io.itmca.lifepuzzle.domain.ai.service.AiDrivingVideoService;
import io.itmca.lifepuzzle.domain.ai.service.AiGeneratedVideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "AI 비디오")
public class AiDrivingVideoEndpoint {
  
  private final AiDrivingVideoService aiDrivingVideoService;
  private final AiGeneratedVideoService aiGeneratedVideoService;
  
  @Operation(summary = "드라이빙 비디오 목록 조회")
  @GetMapping("/v1/ai/driving-videos")
  public ResponseEntity<AiDrivingVideoResponse> getDrivingVideos() {
    var drivingVideos = aiDrivingVideoService.getAllActiveDrivingVideos();
    var drivingVideoDtos = AiDrivingVideoDto.listFrom(drivingVideos);
    var response = AiDrivingVideoResponse.from(drivingVideoDtos);
    
    return ResponseEntity.ok(response);
  }
  
  @Operation(summary = "갤러리 이미지로 생성된 AI 비디오 목록 조회")
  @GetMapping("/v1/ai/videos")
  public ResponseEntity<AiGeneratedVideoResponse> getGeneratedVideosByGallery(@RequestParam("galleryId") Long galleryId) {
    var generatedVideos = aiGeneratedVideoService.getGeneratedVideosByGalleryId(galleryId);
    var generatedVideoDtos = AiGeneratedVideoDto.listFrom(generatedVideos);
    var response = AiGeneratedVideoResponse.from(generatedVideoDtos);
    
    return ResponseEntity.ok(response);
  }
  
  @Operation(summary = "AI 포토 생성")
  @PostMapping("/v1/ai/videos")
  public ResponseEntity<Void> generateAiPhoto(@RequestBody AiPhotoGenerateRequest request) {
    // TODO: 비즈니스 로직 구현
    return ResponseEntity.ok().build();
  }
}