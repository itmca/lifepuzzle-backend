package io.itmca.lifepuzzle.domain.question.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class QuestionStoryQueryRepository {
    private final EntityManager entityManager;

    public QuestionStoryQueryRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void getByHeroNo(int heroNo) {}
}
