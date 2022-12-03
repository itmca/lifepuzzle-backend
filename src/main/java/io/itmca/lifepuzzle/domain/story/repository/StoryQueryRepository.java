package io.itmca.lifepuzzle.domain.story.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class StoryQueryRepository {
    private final EntityManager entityManager;

    public StoryQueryRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void findByHeroNo(int heroNo){};
    public void findById(String storyKey){};
}
