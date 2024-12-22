package io.itmca.lifepuzzle.domain.gallery.endpoint.response.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AgeGroupGalleryDTO {
  private int startYear;
  private int endYear;
  private int galleryCount;
  private List<GalleryDTO> gallery;
}
