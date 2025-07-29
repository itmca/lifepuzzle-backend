package io.itmca.lifepuzzle.domain.story.endpoint.response;

import io.itmca.lifepuzzle.domain.hero.endpoint.response.dto.HeroDto;
import io.itmca.lifepuzzle.domain.story.endpoint.response.dto.AgeGroupGalleryDto;
import io.itmca.lifepuzzle.domain.story.endpoint.response.dto.TagDto;
import io.itmca.lifepuzzle.domain.story.type.AgeGroup;
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
  private HeroDto hero;
  private Map<AgeGroup, AgeGroupGalleryDto> ageGroups;
  private List<TagDto> tags;
  private int totalGallery;
}

