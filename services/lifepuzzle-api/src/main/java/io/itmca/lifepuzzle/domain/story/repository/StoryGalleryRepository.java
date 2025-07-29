package io.itmca.lifepuzzle.domain.story.repository;

import io.itmca.lifepuzzle.domain.story.entity.StoryGallery;
import io.itmca.lifepuzzle.domain.story.entity.StoryGalleryMapId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryGalleryRepository extends JpaRepository<StoryGallery, StoryGalleryMapId> {
}
