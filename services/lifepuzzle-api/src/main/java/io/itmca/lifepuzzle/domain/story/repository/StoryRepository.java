package io.itmca.lifepuzzle.domain.story.repository;

import io.itmca.lifepuzzle.domain.story.entity.Story;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryRepository extends JpaRepository<Story, String> {
  @Query("SELECT s FROM Story s WHERE s.heroId = :heroNo "
      + "ORDER BY s.createdAt DESC")
  Optional<List<Story>> findAllByHeroNo(@Param("heroNo") Long heroNo);

  @Query("SELECT s FROM Story s LEFT JOIN FETCH s.likes l WHERE s.id = :storyKey")
  Optional<Story> findByStoryKey(@Param("storyKey") String storyKey);

  int countByHeroId(Long heroNo);
}
