package io.itmca.lifepuzzle.domain.story.service;

import static io.itmca.lifepuzzle.domain.story.type.GalleryType.IMAGE;
import static io.itmca.lifepuzzle.domain.story.type.GalleryType.VIDEO;
import static org.springframework.util.CollectionUtils.isEmpty;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.service.HeroQueryService;
import io.itmca.lifepuzzle.domain.story.entity.Story;
import io.itmca.lifepuzzle.domain.story.entity.StoryPhoto;
import io.itmca.lifepuzzle.domain.story.entity.StoryPhotoMap;
import io.itmca.lifepuzzle.domain.story.file.StoryFile;
import io.itmca.lifepuzzle.domain.story.file.StoryImageFile;
import io.itmca.lifepuzzle.domain.story.file.StoryVideoFile;
import io.itmca.lifepuzzle.domain.story.repository.StoryPhotoRepository;
import io.itmca.lifepuzzle.domain.story.type.AgeGroup;
import io.itmca.lifepuzzle.global.infra.file.service.S3UploadService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
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


  @Deprecated
  @Transactional
  public void savePhotos(Story story, StoryFile storyFile) {
    Long heroId = story.getHeroId();
    Hero hero = heroQueryService.findHeroByHeroNo(heroId);

    if (!isEmpty(storyFile.images())) {
      List<StoryImageFile> images = storyFile.images();

      for (StoryImageFile image : images) {
        StoryPhoto photo = StoryPhoto.builder()
            .heroId(story.getHeroId())
            .ageGroup(story.getTag(hero))
            .url(image.getBase() + image.getFileName())
            .storyMaps(new ArrayList<>())
            .build();

        storyPhotoRepository.save(photo);

        StoryPhotoMap storyPhotoMap = StoryPhotoMap.builder()
            .storyId(story.getId())
            .photoId(photo.getId())
            .story(story)
            .photo(photo)
            .build();

        photo.getStoryMaps().add(storyPhotoMap);
      }
    }
  }

  @Deprecated
  @Transactional
  public void updatePhotos(Story story, StoryFile storyFile) {
    deletePhotos(story);
    savePhotos(story, storyFile);
  }

  @Deprecated
  @Transactional
  public void deletePhotos(Story story) {
    List<StoryPhoto> storyPhotos = story.getPhotoMaps().stream()
        .map(StoryPhotoMap::getPhoto).toList();

    storyPhotoRepository.deleteAll(storyPhotos);
  }
}
