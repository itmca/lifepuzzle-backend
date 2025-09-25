package io.itmca.lifepuzzle.domain.content.repository;

import io.itmca.lifepuzzle.domain.content.entity.StoryGallery;
import io.itmca.lifepuzzle.domain.content.entity.StoryGalleryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryGalleryRepository extends JpaRepository<StoryGallery, StoryGalleryId> {
}
