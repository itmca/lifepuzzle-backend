package io.itmca.lifepuzzle.domain.story.service;

import io.itmca.lifepuzzle.domain.story.entity.Story;
import io.itmca.lifepuzzle.domain.story.repository.StoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoryQueryService {

    private final StoryRepository storyRepository;

    public List<Story> findStoriesByHeroId(Long heroNo){
        return storyRepository.findAllByHeroNo(heroNo);
    };

    public Story findById(String storyKey){
        return storyRepository.findById(storyKey).get();
    };
}
