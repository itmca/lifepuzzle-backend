package io.itmca.lifepuzzle.domain.story.service;

import static org.springframework.util.CollectionUtils.isEmpty;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.service.HeroQueryService;
import io.itmca.lifepuzzle.domain.story.entity.Story;
import io.itmca.lifepuzzle.domain.story.entity.StoryPhoto;
import io.itmca.lifepuzzle.domain.story.entity.StoryPhotoMap;
import io.itmca.lifepuzzle.domain.story.file.StoryFile;
import io.itmca.lifepuzzle.domain.story.file.StoryImageFile;
import io.itmca.lifepuzzle.domain.story.repository.StoryPhotoRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoryPhotoService {
  private final StoryPhotoRepository storyPhotoRepository;
  private final HeroQueryService heroQueryService;

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

  @Transactional
  public void updatePhotos(Story story, StoryFile storyFile) {
    deletePhotos(story);
    savePhotos(story, storyFile);
  }

  @Transactional
  public void deletePhotos(Story story) {
    List<StoryPhoto> storyPhotos = story.getPhotoMaps().stream()
        .map(StoryPhotoMap::getPhoto).toList();

    storyPhotoRepository.deleteAll(storyPhotos);
  }
}
