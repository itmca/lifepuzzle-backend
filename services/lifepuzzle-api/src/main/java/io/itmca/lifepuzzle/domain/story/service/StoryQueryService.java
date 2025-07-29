package io.itmca.lifepuzzle.domain.story.service;

import io.itmca.lifepuzzle.domain.story.entity.Story;
import io.itmca.lifepuzzle.domain.story.repository.StoryRepository;
import io.itmca.lifepuzzle.global.exception.StoryNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StoryQueryService {

  private final StoryRepository storyRepository;

  public List<Story> findStoriesByHeroId(Long heroNo) {
    return storyRepository.findAllByHeroNo(heroNo)
        .orElseThrow(() -> StoryNotFoundException.byHeroNo(heroNo));
  }

  public Story findById(String storyKey) {
    return storyRepository.findByStoryKey(storyKey)
        .orElseThrow(() -> StoryNotFoundException.byStoryId(storyKey));
  }

  public int countByHeroNo(Long heroNo) {
    return storyRepository.countByHeroId(heroNo);
  }
}
