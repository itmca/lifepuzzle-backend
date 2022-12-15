package io.itmca.lifepuzzle.domain.story.service;

import io.itmca.lifepuzzle.domain.story.entity.Story;
import io.itmca.lifepuzzle.domain.story.repository.StoryWriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoryWriteService {
    private final StoryWriteRepository storyWriteRepository;

    public Story create(Story story){
        return storyWriteRepository.save(story);
    };
    public void update(Story story){
        storyWriteRepository.save(story);
    };
}
