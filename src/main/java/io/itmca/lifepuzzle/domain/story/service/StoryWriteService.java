package io.itmca.lifepuzzle.domain.story.service;

import static io.itmca.lifepuzzle.global.constants.FileConstant.STORY_BASE_PATH;
import static java.io.File.separator;
import static org.springframework.util.CollectionUtils.isEmpty;

import io.itmca.lifepuzzle.domain.story.endpoint.request.StoryGalleryWriteRequest;
import io.itmca.lifepuzzle.domain.story.entity.Story;
import io.itmca.lifepuzzle.domain.story.entity.StoryPhotoMap;
import io.itmca.lifepuzzle.domain.story.file.StoryFile;
import io.itmca.lifepuzzle.domain.story.file.StoryVoiceFile;
import io.itmca.lifepuzzle.domain.story.repository.StoryPhotoMapRepository;
import io.itmca.lifepuzzle.domain.story.repository.StoryRepository;
import io.itmca.lifepuzzle.global.exception.StoryNotFoundException;
import io.itmca.lifepuzzle.global.file.CustomFile;
import io.itmca.lifepuzzle.global.file.service.S3UploadService;
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
  private final S3UploadService s3UploadService;
  private final StoryPhotoMapRepository storyPhotoMapRepository;

  @Transactional
  public String create(Story story, List<Long> galleryIds, @Nullable MultipartFile voice) {
    if (voice != null) {
      s3UploadService.upload(new StoryVoiceFile(story, voice));
    }
    story.setVoice(voice);

    var savedStory = storyRepository.save(story);

    var storyPhotoMaps = galleryIds.stream()
        .map(id -> StoryPhotoMap.create(story, id))
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

    story.addStoryFile(storyFile);
  }

  private void saveStoryPhotoMaps(List<StoryPhotoMap> storyPhotoMaps) {
    for (StoryPhotoMap storyPhotoMap : storyPhotoMaps) {
      storyPhotoMapRepository.save(storyPhotoMap);
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
      var imageNames = getFileNamesToDelete(story.getImageNames(), storyFile.images());
      s3UploadService.delete(story.getImageFolder(), imageNames);
    }

    if (!isEmpty(storyFile.voices())) {
      var voiceNames = getFileNamesToDelete(story.getAudioNames(), storyFile.voices());
      s3UploadService.delete(story.getAudioFolder(), voiceNames);
    }

    if (!isEmpty(storyFile.videos())) {
      var videoNames = getFileNamesToDelete(story.getVideoNames(), storyFile.videos());
      s3UploadService.delete(story.getVideoFolder(), videoNames);
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
}
