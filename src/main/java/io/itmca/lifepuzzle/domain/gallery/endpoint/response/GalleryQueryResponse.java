package io.itmca.lifepuzzle.domain.gallery.endpoint.response;

import io.itmca.lifepuzzle.domain.gallery.endpoint.response.dto.AgeGroupGalleryDTO;
import io.itmca.lifepuzzle.domain.gallery.endpoint.response.dto.TagDTO;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.dto.HeroDTO;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GalleryQueryResponse {
  private HeroDTO hero;
  private Map<String, AgeGroupGalleryDTO> ageGroups;
  private List<TagDTO> tags;
}
