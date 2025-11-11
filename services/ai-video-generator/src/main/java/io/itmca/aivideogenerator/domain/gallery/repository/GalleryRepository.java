package io.itmca.aivideogenerator.domain.gallery.repository;

import io.itmca.aivideogenerator.domain.gallery.entity.Gallery;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {
  Optional<Gallery> findById(Long id);
}