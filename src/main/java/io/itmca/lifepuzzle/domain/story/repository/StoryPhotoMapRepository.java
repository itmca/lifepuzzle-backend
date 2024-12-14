package io.itmca.lifepuzzle.domain.story.repository;

import io.itmca.lifepuzzle.domain.story.entity.StoryPhotoMap;
import io.itmca.lifepuzzle.domain.story.entity.StoryPhotoMapId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryPhotoMapRepository extends JpaRepository<StoryPhotoMap, StoryPhotoMapId> {
}
