package io.itmca.lifepuzzle.domain.question.repository;

import io.itmca.lifepuzzle.domain.question.entity.QuestionStory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionStoryRepository extends JpaRepository<QuestionStory, Long> {
    List<QuestionStory> findByHeroNo(Long heroNo);
}
