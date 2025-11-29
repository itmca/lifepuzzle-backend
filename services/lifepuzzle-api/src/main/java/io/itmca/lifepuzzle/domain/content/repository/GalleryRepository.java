package io.itmca.lifepuzzle.domain.content.repository;

import io.itmca.lifepuzzle.domain.content.entity.Gallery;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {
  Optional<List<Gallery>> findByHeroId(Long heroId);
  
  Optional<Gallery> findByUrl(String url);

  @Query("SELECT g FROM Gallery g "
         + "LEFT JOIN FETCH g.storyMaps sm "
         + "LEFT JOIN FETCH sm.story "
         + "WHERE g.heroId = :heroId "
         + "AND g.ageGroup IN :ageGroups")
  Optional<List<Gallery>> findByHeroIdAndAgeGroupsWithStories(@Param("heroId") Long heroId, 
                                                              @Param("ageGroups") List<io.itmca.lifepuzzle.domain.content.type.AgeGroup> ageGroups);
}
