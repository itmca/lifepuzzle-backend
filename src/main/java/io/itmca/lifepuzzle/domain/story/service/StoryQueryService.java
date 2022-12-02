package io.itmca.lifepuzzle.domain.story.service;

import io.itmca.lifepuzzle.domain.story.repository.StoryQueryRepository;

public class StoryQueryService {

    private final StoryQueryRepository storyQueryRepository;

    public StoryQueryService(StoryQueryRepository storyQueryRepository) {
        this.storyQueryRepository = storyQueryRepository;
    }

    public void findByHeroNo(int heroNo){};
    public void findById(String storyKey){};
}
