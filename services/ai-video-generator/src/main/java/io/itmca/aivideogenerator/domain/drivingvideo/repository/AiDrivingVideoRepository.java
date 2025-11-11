package io.itmca.aivideogenerator.domain.drivingvideo.repository;

import io.itmca.aivideogenerator.domain.drivingvideo.entity.AiDrivingVideo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AiDrivingVideoRepository extends JpaRepository<AiDrivingVideo, Long> {
  
  @Query("SELECT d FROM AiDrivingVideo d WHERE d.id = :id AND d.deletedAt IS NULL")
  Optional<AiDrivingVideo> findActiveById(@Param("id") Long id);
}