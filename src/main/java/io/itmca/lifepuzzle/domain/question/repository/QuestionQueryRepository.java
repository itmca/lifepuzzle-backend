package io.itmca.lifepuzzle.domain.question.repository;

import javax.persistence.EntityManager;

public class QuestionQueryRepository {
    private final EntityManager entityManager;

    public void getAll(){}
    public void getById(String questionNo){}
    public void getByCategory(String category){}
}
