package io.itmca.lifepuzzle.domain.story.service;

import static org.springframework.util.CollectionUtils.isEmpty;

import io.itmca.lifepuzzle.domain.story.entity.Story;
import io.itmca.lifepuzzle.domain.story.file.StoryFile;
import io.itmca.lifepuzzle.domain.story.repository.StoryRepository;
import io.itmca.lifepuzzle.global.infra.file.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

  public void update(Story story) {
    storyRepository.save(story);
  }
}
