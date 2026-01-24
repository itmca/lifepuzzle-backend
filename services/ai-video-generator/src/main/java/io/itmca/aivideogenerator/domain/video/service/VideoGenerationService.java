package io.itmca.aivideogenerator.domain.video.service;

import io.itmca.aivideogenerator.domain.video.entity.AiGeneratedVideo;
import io.itmca.aivideogenerator.domain.video.event.AiVideoCreateEvent;
import io.itmca.aivideogenerator.domain.video.repository.AiGeneratedVideoRepository;
import io.itmca.aivideogenerator.global.service.PythonExecutorService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoGenerationService {

  private final AiGeneratedVideoRepository aiGeneratedVideoRepository;
  private final PythonExecutorService pythonExecutorService;

  @Transactional
  public void processVideoGeneration(AiVideoCreateEvent event) {
    log.info("Processing video generation for heroId: {}, galleryId: {}, drivingVideoId: {}",
        event.getHeroId(), event.getGalleryId(), event.getDrivingVideoId());

    Optional<AiGeneratedVideo> existingVideo = aiGeneratedVideoRepository
        .findByHeroNoAndGalleryIdAndDrivingVideoId(
            event.getHeroId(),
            event.getGalleryId(),
            event.getDrivingVideoId()
        );

    AiGeneratedVideo video;
    if (existingVideo.isPresent()) {
      video = existingVideo.get();
      log.info("Found existing video record with id: {}", video.getId());
    } else {
      video = AiGeneratedVideo.builder()
          .heroNo(event.getHeroId())
          .galleryId(event.getGalleryId())
          .drivingVideoId(event.getDrivingVideoId())
          .build();
      video = aiGeneratedVideoRepository.save(video);
      log.info("Created new video record with id: {}", video.getId());
    }

    try {
      video.markAsStarted();
      aiGeneratedVideoRepository.save(video);
      log.info("Marked video {} as IN_PROGRESS", video.getId());

      // Python 명령어 실행을 위한 준비
      pythonExecutorService.prepareVideoGeneration(video);

    } catch (Exception e) {
      log.error("Video generation failed for videoId: {}", video.getId(), e);
      video.markAsFailed(e.getMessage());
      aiGeneratedVideoRepository.save(video);
    }
  }

  public List<AiGeneratedVideo> getVideosByHero(Long heroNo) {
    return aiGeneratedVideoRepository.findByHeroNoOrderByCreatedAtDesc(heroNo);
  }
}