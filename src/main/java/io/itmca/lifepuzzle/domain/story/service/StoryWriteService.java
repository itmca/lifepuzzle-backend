package io.itmca.lifepuzzle.domain.story.service;

import io.itmca.lifepuzzle.domain.story.repository.StoryWriteRepository;

public class StoryWriteService {
    private final StoryWriteRepository storyWriteRepository;

    public StoryWriteService(StoryWriteRepository storyWriteRepository) {
        this.storyWriteRepository = storyWriteRepository;
    }

    public void create(Story story){};
    public void update(Story story){};
}
