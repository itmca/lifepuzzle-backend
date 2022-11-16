package io.itmca.lifepuzzle.domain.story.repository;

import io.itmca.lifepuzzle.domain.story.entity.Story;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class StoryWriteRepository {
    private final EntityManager entityManager;

    public StoryWriteRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Story story){};
    public void update(Story story){};
}
