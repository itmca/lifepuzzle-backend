package io.itmca.lifepuzzle.domain.gallery.repository;

import io.itmca.lifepuzzle.domain.story.entity.Gallery;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {
  Optional<List<Gallery>> findByHeroId(Long heroId);
}
