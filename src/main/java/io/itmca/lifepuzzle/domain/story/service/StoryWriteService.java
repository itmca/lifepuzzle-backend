package io.itmca.lifepuzzle.domain.story.service;

import static io.itmca.lifepuzzle.global.constant.FileConstant.STORY_BASE_PATH;
import static java.io.File.separator;
import static org.springframework.util.CollectionUtils.isEmpty;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.repository.HeroRepository;
import io.itmca.lifepuzzle.domain.story.entity.Story;
import io.itmca.lifepuzzle.domain.story.entity.StoryPhoto;
import io.itmca.lifepuzzle.domain.story.file.StoryFile;
import io.itmca.lifepuzzle.domain.story.repository.StoryRepository;
import io.itmca.lifepuzzle.global.infra.file.CustomFile;
import io.itmca.lifepuzzle.global.infra.file.service.S3UploadService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoryWriteService {
  private final StoryRepository storyRepository;
  private final S3UploadService s3UploadService;

  public Story create(Story story, StoryFile storyFile) {
    uploadStoryFile(storyFile);

    story.addStoryFile(storyFile);

    return storyRepository.save(story);
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

  @Transactional
  public void update(Story story, StoryFile storyFile) {
    // TODO 2023.09.09 Solmioh 삭제 로직 확인 필요
    deleteStoryFile(story, storyFile);

    uploadStoryFile(storyFile);

    story.addStoryFile(storyFile);
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
