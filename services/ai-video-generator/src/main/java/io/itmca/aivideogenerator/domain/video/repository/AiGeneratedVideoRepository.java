package io.itmca.aivideogenerator.domain.video.repository;

import io.itmca.aivideogenerator.domain.video.entity.AiGeneratedVideo;
import io.itmca.aivideogenerator.domain.video.type.VideoGenerationStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiGeneratedVideoRepository extends JpaRepository<AiGeneratedVideo, Long> {

  List<AiGeneratedVideo> findByHeroNoOrderByCreatedAtDesc(Long heroNo);

  List<AiGeneratedVideo> findByStatus(VideoGenerationStatus status);

  Optional<AiGeneratedVideo> findByHeroNoAndGalleryIdAndDrivingVideoId(
      Long heroNo, Long galleryId, Long drivingVideoId);
}