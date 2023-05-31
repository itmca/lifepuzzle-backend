package io.itmca.lifepuzzle.domain.story.service;

import static io.itmca.lifepuzzle.global.constant.FileConstant.FILE_NAMES_SEPARATOR;

import io.itmca.lifepuzzle.domain.story.entity.Story;
import io.itmca.lifepuzzle.domain.story.repository.StoryRepository;
import io.itmca.lifepuzzle.global.infra.file.CustomFile;
import io.itmca.lifepuzzle.global.infra.file.ImageCustomFile;
import io.itmca.lifepuzzle.global.infra.file.repository.S3Repository;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
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

  public void saveFile(Story story, List<? extends CustomFile> customFiles) throws IOException {
    for (CustomFile customFile : customFiles) {
      s3Repository.upload(customFile);
    }

    addFileInStory(story, customFiles);
  }

  private void addFileInStory(Story story, List<? extends CustomFile> customFiles) {
    if (customFiles.isEmpty()) {
      return;
    }

    var firstFile = customFiles.get(0);
    var basePath = firstFile.getBasePath();
    var fileNames = customFiles
        .stream()
        .map(customFile -> customFile.getFileName())
        .collect(Collectors.joining(FILE_NAMES_SEPARATOR));

    if (firstFile instanceof ImageCustomFile) {
      story.setImage(basePath, fileNames);
    } else {
      story.setAudio(basePath, fileNames);
    }
  }
}
