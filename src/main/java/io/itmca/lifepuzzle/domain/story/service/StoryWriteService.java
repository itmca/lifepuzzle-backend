package io.itmca.lifepuzzle.domain.story.service;

import static io.itmca.lifepuzzle.global.constant.FileConstant.STORY_BASE_PATH;

import io.itmca.lifepuzzle.domain.story.entity.Story;
import io.itmca.lifepuzzle.domain.story.repository.StoryRepository;
import io.itmca.lifepuzzle.global.infra.file.CustomFile;
import io.itmca.lifepuzzle.global.infra.file.ImageCustomFile;
import io.itmca.lifepuzzle.global.infra.file.VoiceCustomFile;
import io.itmca.lifepuzzle.global.infra.file.repository.S3Repository;
import java.io.File;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoryWriteService {
  private final StoryRepository storyRepository;
  private final S3Repository s3Repository;

  public Story create(Story story) {
    return storyRepository.save(story);
  }

  public void update(Story story) {
    storyRepository.save(story);
  }

  public void saveImage(Story story, List<ImageCustomFile> imageFiles) throws IOException {
    saveFile(story, imageFiles);
    story.addImage(imageFiles);
  }

  public void saveVoice(Story story, List<VoiceCustomFile> voiceFiles) throws IOException {
    saveFile(story, voiceFiles);
    story.addVoice(voiceFiles);
  }

  private void saveFile(Story story, List<? extends CustomFile> customFiles) throws IOException {
    var base = STORY_BASE_PATH + File.separator + story.getStoryKey();

    for (CustomFile customFile : customFiles) {
      s3Repository.upload(customFile, base);
    }
  }
}
