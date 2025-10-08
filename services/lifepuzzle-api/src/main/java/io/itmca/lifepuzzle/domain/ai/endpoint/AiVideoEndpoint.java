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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "AI 포토", description = "사진으로 AI 비디오를 생성하는 기능")
public class AiVideoEndpoint {
  
  private final AiDrivingVideoService aiDrivingVideoService;
  private final AiGeneratedVideoService aiGeneratedVideoService;
  
  @Operation(summary = "드라이빙 비디오 목록 조회", description = "사용 가능한 드라이빙 비디오 템플릿 목록을 조회합니다")
  @GetMapping("/v1/ai/driving-videos")
  public ResponseEntity<AiDrivingVideoResponse> getDrivingVideos() {
    var drivingVideos = aiDrivingVideoService.getAllActiveDrivingVideos();
    var drivingVideoDtos = AiDrivingVideoDto.listFrom(drivingVideos);
    var response = AiDrivingVideoResponse.from(drivingVideoDtos);
    
    return ResponseEntity.ok(response);
  }
  
  @Operation(summary = "AI 포토 작업 내역 조회", description = "특정 주인공의 AI 포토(비디오) 생성 작업 내역을 조회합니다")
  @GetMapping("/v1/ai/videos")
  public ResponseEntity<AiGeneratedVideoResponse> getGeneratedVideosByHero(@RequestParam("heroNo") Long heroNo) {
    var generatedVideos = aiGeneratedVideoService.getGeneratedVideosByHeroNo(heroNo);
    var generatedVideoDtos = AiGeneratedVideoDto.listFrom(generatedVideos);
    var response = AiGeneratedVideoResponse.from(generatedVideoDtos);
    
    return ResponseEntity.ok(response);
  }
  
  @Operation(summary = "AI 포토 생성", description = "업로드된 사진을 기반으로 AI 비디오를 생성합니다")
  @PostMapping("/v1/ai/videos")
  public ResponseEntity<Void> generateAiVideo(@RequestBody AiPhotoGenerateRequest request) {
    // TODO: AI 비디오 생성 비즈니스 로직 구현
    return ResponseEntity.ok().build();
  }
}