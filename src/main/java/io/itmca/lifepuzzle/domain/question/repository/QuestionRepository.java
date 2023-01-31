package io.itmca.lifepuzzle.domain.question.repository;

import io.itmca.lifepuzzle.domain.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAll();
    List<Question> findByCategory(String category);
}
