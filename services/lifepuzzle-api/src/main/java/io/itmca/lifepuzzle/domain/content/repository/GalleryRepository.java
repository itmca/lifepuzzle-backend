package io.itmca.lifepuzzle.domain.content.repository;

import io.itmca.lifepuzzle.domain.content.entity.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {
  Optional<List<Gallery>> findByHeroId(Long heroId);
}
