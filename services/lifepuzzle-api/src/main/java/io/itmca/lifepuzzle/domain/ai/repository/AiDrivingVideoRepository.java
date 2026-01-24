package io.itmca.lifepuzzle.domain.ai.repository;

import io.itmca.lifepuzzle.domain.ai.entity.AiDrivingVideo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AiDrivingVideoRepository extends JpaRepository<AiDrivingVideo, Long> {
  
  @Query("SELECT a FROM AiDrivingVideo a WHERE a.deletedAt IS NULL ORDER BY a.priority ASC, a.createdAt DESC")
  List<AiDrivingVideo> findAllActiveOrderByCreatedAtDesc();
}