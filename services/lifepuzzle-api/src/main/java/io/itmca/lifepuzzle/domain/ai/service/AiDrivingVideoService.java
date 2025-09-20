package io.itmca.lifepuzzle.domain.ai.service;

import io.itmca.lifepuzzle.domain.ai.entity.AiDrivingVideo;
import io.itmca.lifepuzzle.domain.ai.repository.AiDrivingVideoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiDrivingVideoService {
  
  private final AiDrivingVideoRepository aiDrivingVideoRepository;
  
  public List<AiDrivingVideo> getAllActiveDrivingVideos() {
    return aiDrivingVideoRepository.findAllActiveOrderByCreatedAtDesc();
  }
}