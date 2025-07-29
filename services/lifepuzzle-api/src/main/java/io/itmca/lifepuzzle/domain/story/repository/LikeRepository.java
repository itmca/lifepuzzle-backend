package io.itmca.lifepuzzle.domain.story.repository;

import io.itmca.lifepuzzle.domain.story.entity.Like;
import io.itmca.lifepuzzle.domain.story.type.LikeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends JpaRepository<Like, Long> {
  @Query("SELECT l FROM Like l WHERE l.id = :userId AND l.targetId = :targetId AND l.type = :type")
  Optional<Like> findLike(@Param("userId") Long userId, @Param("targetId") String targetId,
                          @Param("type") LikeType type);
}
