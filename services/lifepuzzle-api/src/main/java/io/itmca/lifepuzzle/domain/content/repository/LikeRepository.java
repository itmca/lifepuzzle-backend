package io.itmca.lifepuzzle.domain.content.repository;

import io.itmca.lifepuzzle.domain.content.entity.Like;
import io.itmca.lifepuzzle.domain.content.type.LikeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends JpaRepository<Like, Long> {
  @Query("SELECT l FROM Like l WHERE l.userId = :userId AND l.contentId = :contentId AND l.type = :type")
  Optional<Like> findLike(@Param("userId") Long userId, @Param("contentId") String contentId,
                          @Param("type") LikeType type);
}
