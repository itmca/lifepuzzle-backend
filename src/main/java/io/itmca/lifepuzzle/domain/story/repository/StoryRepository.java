package io.itmca.lifepuzzle.domain.story.repository;

import io.itmca.lifepuzzle.domain.story.entity.Story;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryRepository extends JpaRepository<Story, String> {
  @Query("SELECT s FROM Story s WHERE s.heroId = :heroNo "
      + "ORDER BY s.createdAt DESC")
  List<Story> findAllByHeroNo(@Param("heroNo") Long heroNo);

  int countByHeroId(Long heroNo);
}
