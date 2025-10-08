package io.itmca.lifepuzzle.domain.ai.service;

import io.itmca.lifepuzzle.domain.ai.entity.AiGeneratedVideo;
import io.itmca.lifepuzzle.domain.ai.repository.AiGeneratedVideoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiGeneratedVideoService {
  
  private final AiGeneratedVideoRepository aiGeneratedVideoRepository;
  
  public List<AiGeneratedVideo> getGeneratedVideosByHeroNo(Long heroNo) {
    return aiGeneratedVideoRepository.findByHeroNoAndNotDeleted(heroNo);
  }
  
  public List<AiGeneratedVideo> getAllGeneratedVideos() {
    return aiGeneratedVideoRepository.findAllActiveOrderByCreatedAtDesc();
  }
}