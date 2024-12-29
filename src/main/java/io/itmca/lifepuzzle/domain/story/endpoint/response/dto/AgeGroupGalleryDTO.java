package io.itmca.lifepuzzle.domain.story.endpoint.response.dto;

import static java.util.Comparator.comparingInt;

import io.itmca.lifepuzzle.domain.story.entity.StoryPhoto;
import io.itmca.lifepuzzle.domain.story.type.AgeGroup;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
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

  public static AgeGroupGalleryDTO from(List<StoryPhoto> photos, LocalDate birthdate, AtomicInteger index) {
    var ageGroup = photos.getFirst().getAgeGroup();
    var startYear = ageGroup.getStartYear(birthdate);
    var endYear = ageGroup.getEndYear(birthdate);

    var galleryList = photos.stream()
        .map(photo -> GalleryDTO.from(photo, index.getAndIncrement()))
        .toList();

    return AgeGroupGalleryDTO.builder()
        .startYear(startYear)
        .endYear(endYear)
        .galleryCount(photos.size())
        .gallery(galleryList)
        .build();
  }

  public static Map<String, AgeGroupGalleryDTO> fromGroupedGallery(Map<AgeGroup, List<StoryPhoto>> groupedPhotos, LocalDate birthdate) {
    var index = new AtomicInteger(1);
    return groupedPhotos.entrySet().stream()
        .sorted(comparingInt(entry -> entry.getKey().getRepresentativeAge()))
        .collect(Collectors.toMap(
            entry -> entry.getKey().getTagKey(),
            entry -> AgeGroupGalleryDTO.from(entry.getValue(), birthdate, index),
            (oldValue, newValue) -> oldValue,
            LinkedHashMap::new
        ));
  }
}
