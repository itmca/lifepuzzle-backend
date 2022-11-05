package io.itmca.lifepuzzle.domain.story.repository;

import javax.persistence.EntityManager;

public class StoryWriteRepository {
    private final EntityManager entityManager;

    public void create(Story story){};
    public void update(Story story){};
}
