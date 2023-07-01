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

  private final MultipartFile multipartFile;
  private final byte[] bytes;
  private final String base;
  private final String fileName;

  public StoryVoiceFile(Story story, MultipartFile multipartFile) {
    this.multipartFile = multipartFile;
    this.fileName = addRandomValueFilePrefix(multipartFile);
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
