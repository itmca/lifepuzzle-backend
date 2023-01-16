package io.itmca.lifepuzzle.domain.story.repository;

import io.itmca.lifepuzzle.domain.story.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryWriteRepository extends JpaRepository<Story, String> {
}
