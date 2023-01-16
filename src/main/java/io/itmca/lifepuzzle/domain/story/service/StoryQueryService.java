package io.itmca.lifepuzzle.domain.story.service;

import io.itmca.lifepuzzle.domain.story.entity.Story;
import io.itmca.lifepuzzle.domain.story.repository.StoryQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoryQueryService {

    private final StoryQueryRepository storyQueryRepository;

    public List<Story> findStoriesByHeroId(Long heroNo){
        return storyQueryRepository.findAllByHeroNo(heroNo);
    };

    public Story findById(String storyKey){
        return storyQueryRepository.findById(storyKey).get();
    };
}
