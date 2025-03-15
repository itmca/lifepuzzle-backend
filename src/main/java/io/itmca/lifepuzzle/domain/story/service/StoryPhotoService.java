package io.itmca.lifepuzzle.domain.story.service;

import static io.itmca.lifepuzzle.domain.story.type.GalleryType.IMAGE;
import static io.itmca.lifepuzzle.domain.story.type.GalleryType.VIDEO;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import io.itmca.lifepuzzle.domain.hero.endpoint.response.dto.HeroDto;
import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.service.HeroQueryService;
import io.itmca.lifepuzzle.domain.story.endpoint.response.GalleryQueryResponse;
import io.itmca.lifepuzzle.domain.story.endpoint.response.dto.AgeGroupGalleryDto;
import io.itmca.lifepuzzle.domain.story.endpoint.response.dto.TagDto;
import io.itmca.lifepuzzle.domain.story.entity.StoryPhoto;
import io.itmca.lifepuzzle.domain.story.file.StoryImageFile;
import io.itmca.lifepuzzle.domain.story.file.StoryVideoFile;
import io.itmca.lifepuzzle.domain.story.repository.StoryPhotoRepository;
import io.itmca.lifepuzzle.domain.story.type.AgeGroup;
import io.itmca.lifepuzzle.global.exception.GalleryItemNotFoundException;
import io.itmca.lifepuzzle.global.exception.GalleryNotFoundException;
import io.itmca.lifepuzzle.global.file.service.S3UploadService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoryPhotoService {
  private final StoryPhotoRepository storyPhotoRepository;
  private final HeroQueryService heroQueryService;
  private final S3UploadService s3UploadService;

  @Transactional
  public void saveGallery(Long heroId, List<MultipartFile> gallery, AgeGroup ageGroup) {
    List<StoryImageFile> storyImageFiles = StoryImageFile.listFrom(gallery, heroId);
    List<StoryVideoFile> storyVideoFiles = StoryVideoFile.listFrom(gallery, heroId);
    List<StoryPhoto> saveGalleryFiles = new ArrayList<>();

    saveGalleryFiles.addAll(StoryPhoto.listFrom(storyImageFiles, heroId, ageGroup, IMAGE));
    saveGalleryFiles.addAll(StoryPhoto.listFrom(storyVideoFiles, heroId, ageGroup, VIDEO));

    s3UploadService.upload(storyImageFiles);
    s3UploadService.upload(storyVideoFiles);

    storyPhotoRepository.saveAll(saveGalleryFiles);
  }

  @Transactional
  public void deleteGalleryItem(Long galleryId) {
    StoryPhoto storyPhoto = storyPhotoRepository.findById(galleryId).orElse(null);
    if (storyPhoto == null) {
      throw GalleryItemNotFoundException.of(galleryId);
    }
    s3UploadService.delete(storyPhoto.getUrl());
    storyPhotoRepository.delete(storyPhoto);
  }

  public GalleryQueryResponse getHeroGallery(Long heroNo) {
    var hero = heroQueryService.findHeroByHeroNo(heroNo);
    var heroDTO = HeroDto.from(hero);
    var photos = getFilteredGallery(heroDTO);
    var ageGroupsDTO = getGalleryByAgeGroup(photos, hero);

    return GalleryQueryResponse.builder()
        .hero(heroDTO)
        .ageGroups(ageGroupsDTO)
        .tags(getTags(heroDTO.getAge()))
        .totalGallery(photos.size())
        .build();
  }

  private List<StoryPhoto> getFilteredGallery(HeroDto heroDTO) {
    var photos = getGalleryByHeroId(heroDTO.getId());
    var heroAgeGroup = AgeGroup.of(heroDTO.getAge());

    return photos.stream()
        .filter(photo -> photo.getAgeGroup().getRepresentativeAge()
            <= heroAgeGroup.getRepresentativeAge())
        .toList();
  }

  private List<StoryPhoto> getGalleryByHeroId(Long heroId) {
    return storyPhotoRepository.findByHeroId(heroId)
        .orElseThrow(() -> new GalleryNotFoundException(heroId));
  }

  private Map<AgeGroup, AgeGroupGalleryDto> getGalleryByAgeGroup(List<StoryPhoto> photos,
                                                                 Hero hero) {
    var groupedByAge = photos.stream()
        .collect(groupingBy(
            StoryPhoto::getAgeGroup,
            toList()
        ));

    return AgeGroupGalleryDto.fromGroupedGallery(groupedByAge, hero.getBirthday());
  }

  private List<TagDto> getTags(int age) {
    var heroAgeGroup = AgeGroup.of(age);

    return Arrays.stream(AgeGroup.values())
        .filter(ageGroup -> ageGroup.getRepresentativeAge() <= heroAgeGroup.getRepresentativeAge())
        .sorted(Comparator.comparingInt(AgeGroup::getRepresentativeAge))
        .map(ageGroup -> TagDto.builder()
            .key(ageGroup)
            .label(ageGroup.getDisplayName())
            .build())
        .toList();
  }
}
