package io.itmca.lifepuzzle.domain.story.service;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.story.entity.Story;
import io.itmca.lifepuzzle.domain.story.type.AgeGroup;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class StoryTagService {
  public List<AgeGroup> getDistinctTags(List<Story> stories, Hero hero) {
    return stories.stream()
        .map(story -> story.getTag(hero))
        .distinct()
        .toList();
  }
}
