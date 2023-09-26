package io.itmca.lifepuzzle.domain.story.file;

import static io.itmca.lifepuzzle.global.constant.FileConstant.STORY_BASE_PATH;

import io.itmca.lifepuzzle.domain.story.entity.Story;
import io.itmca.lifepuzzle.global.infra.file.CustomFile;
import java.io.File;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class StoryImageFile extends CustomFile {
  public StoryImageFile(Story story, MultipartFile file) {
    this(story, file, "");
  }

  public StoryImageFile(Story story, MultipartFile file, String postfix) {
    super(
        String.join(File.separator, STORY_BASE_PATH, story.getStoryKey(), "image"),
        file,
        postfix
    );
  }
}
