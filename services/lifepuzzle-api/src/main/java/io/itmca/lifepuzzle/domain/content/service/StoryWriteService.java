package io.itmca.lifepuzzle.domain.content.service;

import static io.itmca.lifepuzzle.global.constants.FileConstant.STORY_BASE_PATH;
import static java.io.File.separator;
import static org.springframework.util.CollectionUtils.isEmpty;

import io.itmca.lifepuzzle.domain.content.endpoint.request.StoryContentUploadRequest;
import io.itmca.lifepuzzle.domain.content.endpoint.request.StoryGalleryWriteRequest;
import io.itmca.lifepuzzle.domain.content.endpoint.request.StoryVoiceUploadRequest;
import io.itmca.lifepuzzle.domain.content.entity.Story;
import io.itmca.lifepuzzle.domain.content.entity.StoryGallery;
import io.itmca.lifepuzzle.domain.content.repository.GalleryRepository;
import io.itmca.lifepuzzle.domain.content.repository.StoryGalleryRepository;
import io.itmca.lifepuzzle.domain.content.repository.StoryRepository;
import io.itmca.lifepuzzle.global.exception.GalleryNotFoundException;
import io.itmca.lifepuzzle.global.exception.StoryNotFoundException;
import io.itmca.lifepuzzle.global.file.CustomFile;
import io.itmca.lifepuzzle.global.file.domain.StoryFile;
import io.itmca.lifepuzzle.global.file.domain.StoryVoiceFile;
import io.itmca.lifepuzzle.global.file.service.S3UploadService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class StoryWriteService {
  private final StoryRepository storyRepository;
  private final GalleryRepository galleryRepository;
  private final S3UploadService s3UploadService;
  private final StoryGalleryRepository storyGalleryRepository;

  @Transactional
  public String create(Story story, List<Long> galleryIds, @Nullable MultipartFile voice) {
    if (voice != null) {
      s3UploadService.upload(new StoryVoiceFile(story, voice));
    }
    story.setVoice(voice);

    var savedStory = storyRepository.save(story);

    var storyPhotoMaps = galleryIds.stream()
        .map(id -> StoryGallery.create(story, id))
        .toList();
    saveStoryPhotoMaps(storyPhotoMaps);

    return savedStory.getId();
  }

  @Transactional
  public void update(String storyId, StoryGalleryWriteRequest storyGalleryWriteRequest,
                     @Nullable MultipartFile voice) {
    var story = storyRepository.findById(storyId)
        .orElseThrow(() -> StoryNotFoundException.byStoryId(storyId));

    story.update(storyGalleryWriteRequest);

    if (voice != null) {
      s3UploadService.upload(new StoryVoiceFile(story, voice));
    }
    story.setVoice(voice);
  }

  @Transactional
  public void update(Story story, StoryFile storyFile) {
    // TODO 2023.09.09 Solmioh 삭제 로직 확인 필요
    deleteStoryFile(story, storyFile);

    uploadStoryFile(storyFile);

    // TODO: Gallery 시스템으로 이관됨 - 별도 구현 필요
  }

  private void saveStoryPhotoMaps(List<StoryGallery> storyGalleries) {
    for (StoryGallery storyGallery : storyGalleries) {
      storyGalleryRepository.save(storyGallery);
    }
  }

  private void uploadStoryFile(StoryFile storyFile) {
    if (!isEmpty(storyFile.images())) {
      s3UploadService.upload(storyFile.images());
    }

    if (!isEmpty(storyFile.voices())) {
      s3UploadService.upload(storyFile.voices());
    }

    if (!isEmpty(storyFile.videos())) {
      s3UploadService.upload(storyFile.videos());
    }
  }

  private void deleteStoryFile(Story story, StoryFile storyFile) {
    if (!isEmpty(storyFile.images())) {
      // TODO: Gallery 시스템으로 이관됨 - 별도 구현 필요
    }

    if (!isEmpty(storyFile.voices())) {
      var voiceNames = getFileNamesToDelete(story.getAudioNames(), storyFile.voices());
      s3UploadService.delete(story.getAudioFolder(), voiceNames);
    }

    if (!isEmpty(storyFile.videos())) {
      // TODO: Gallery 시스템으로 이관됨 - 별도 구현 필요
    }
  }

  private List<String> getFileNamesToDelete(List<String> oldFileNames,
                                            List<? extends CustomFile> newFiles) {
    var newFileNames = newFiles.stream()
        .map(file -> file.getFileName())
        .toList();

    return oldFileNames.stream()
        .filter(fileName -> !newFileNames.contains(fileName))
        .toList();
  }

  public void delete(String storyKey) {
    s3UploadService.delete(String.join(separator, STORY_BASE_PATH, storyKey));

    storyRepository.deleteById(storyKey);
  }

  @Transactional
  public String upsertContent(StoryContentUploadRequest request, Long userId) {
    var story = findOrCreateStory(request.heroId(), request.galleryId(), userId);
    story.updateContent(request.content());
    return story.getId();
  }

  @Transactional
  public String upsertVoice(StoryVoiceUploadRequest request, MultipartFile voice, Long userId) {
    var story = findOrCreateStory(request.heroId(), request.galleryId(), userId);

    s3UploadService.upload(new StoryVoiceFile(story, voice));
    story.setVoice(voice);

    return story.getId();
  }

  @Transactional
  public void deleteVoice(StoryVoiceUploadRequest request, Long userId) {
    var story = storyRepository.findByHeroIdAndGalleryId(request.heroId(), request.galleryId())
        .orElseThrow(() -> StoryNotFoundException.byHeroIdAndGalleryId(request.heroId(), request.galleryId()));

    if (!isEmpty(story.getAudioNames())) {
      s3UploadService.delete(story.getAudioFolder(), story.getAudioNames());
    }

    story.setVoice(null);
  }

  private Story findOrCreateStory(Long heroId, Long galleryId, Long userId) {
    return storyRepository.findByHeroIdAndGalleryId(heroId, galleryId)
        .orElseGet(() -> createStory(heroId, galleryId, userId));
  }

  private Story createStory(Long heroId, Long galleryId, Long userId) {
    galleryRepository.findByIdAndHeroId(galleryId, heroId)
        .orElseThrow(() -> GalleryNotFoundException.byGalleryId(galleryId));

    var story = Story.builder()
        .id(generateStoryKey(heroId))
        .heroId(heroId)
        .userId(userId)
        .build();

    var savedStory = storyRepository.save(story);
    storyGalleryRepository.save(StoryGallery.create(savedStory, galleryId));

    return savedStory;
  }

  private String generateStoryKey(Long heroId) {
    var now = LocalDateTime.now();
    return heroId.toString() + "-" + now.getHour() + now.getMinute() + now.getSecond();
  }
}
