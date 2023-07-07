package io.itmca.lifepuzzle.domain.story.file;

import static io.itmca.lifepuzzle.global.constant.FileConstant.STORY_BASE_PATH;
import static io.itmca.lifepuzzle.global.util.FileUtil.addRandomValueFilePrefix;

import io.itmca.lifepuzzle.domain.story.entity.Story;
import io.itmca.lifepuzzle.global.infra.file.CustomFile;
import java.io.File;
import java.io.IOException;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class StoryVoiceFile implements CustomFile {
  private final byte[] bytes;
  private final String base;
  private final String fileName;
  private final String originalFileName;

  public StoryVoiceFile(Story story, MultipartFile multipartFile) {
    this.fileName = addRandomValueFilePrefix(multipartFile);
    this.originalFileName = multipartFile.getOriginalFilename();
    this.base = String.join(File.separator, STORY_BASE_PATH, story.getStoryKey(), "voice");

    try {
      this.bytes = multipartFile.getBytes();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public CustomFile resize() {
    return null;
  }
}
