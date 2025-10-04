package io.itmca.lifepuzzle.domain.ai.repository;

import io.itmca.lifepuzzle.domain.ai.entity.AiGeneratedVideo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AiGeneratedVideoRepository extends JpaRepository<AiGeneratedVideo, Long> {
  
  @Query("SELECT a FROM AiGeneratedVideo a WHERE a.galleryId = :galleryId AND a.deletedAt IS NULL ORDER BY a.createdAt DESC")
  List<AiGeneratedVideo> findByGalleryIdAndNotDeleted(@Param("galleryId") Long galleryId);
  
  @Query("SELECT a FROM AiGeneratedVideo a WHERE a.deletedAt IS NULL ORDER BY a.createdAt DESC")
  List<AiGeneratedVideo> findAllActiveOrderByCreatedAtDesc();
}