package io.itmca.lifepuzzle.domain.question.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class QuestionQueryRepository {
    private final EntityManager entityManager;

    public QuestionQueryRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void getAll(){}
    public void getById(String questionNo){}
    public void getByCategory(String category){}
}
