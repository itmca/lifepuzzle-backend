package io.itmca.lifepuzzle.domain.story.service;

import io.itmca.lifepuzzle.domain.story.entity.Story;
import io.itmca.lifepuzzle.domain.story.repository.StoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoryQueryService {

  private final StoryRepository storyRepository;

  public List<Story> findStoriesByHeroId(Long heroNo) {
    return storyRepository.findAllByHeroNo(heroNo);
  }

  public Story findById(String storyKey) {
    return storyRepository.findById(storyKey).get();
  }

  public int countByHeroNo(Long heroNo) {
    return storyRepository.countByHeroId(heroNo);
  }
}
