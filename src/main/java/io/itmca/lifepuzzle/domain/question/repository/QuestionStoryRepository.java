package io.itmca.lifepuzzle.domain.question.repository;

import io.itmca.lifepuzzle.domain.question.entity.QuestionStory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionStoryRepository extends JpaRepository<QuestionStory, Long> {
  List<QuestionStory> findByHeroId(Long heroId);
}
