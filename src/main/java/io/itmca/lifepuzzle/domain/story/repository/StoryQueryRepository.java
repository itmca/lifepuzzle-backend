package io.itmca.lifepuzzle.domain.story.repository;

import javax.persistence.EntityManager;

public class StoryQueryRepository {
    private final EntityManager entityManager;

    public void findByHeroNo(int heroNo){};
    public void findById(String storyKey){};
}
