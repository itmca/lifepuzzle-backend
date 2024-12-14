package io.itmca.lifepuzzle.domain.story.repository;

import io.itmca.lifepuzzle.domain.story.entity.StoryPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryPhotoRepository extends JpaRepository<StoryPhoto, Long> {
}
