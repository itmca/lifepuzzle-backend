package io.itmca.lifepuzzle.domain.story.file;

import static io.itmca.lifepuzzle.global.constant.FileConstant.STORY_IMAGE_BASE_PATH_FORMAT;

import io.itmca.lifepuzzle.domain.story.entity.Story;
import io.itmca.lifepuzzle.global.infra.file.CustomFile;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class StoryImageFile extends CustomFile {
  public StoryImageFile(Story story, MultipartFile file) {
    this(story, file, "");
  }

  public StoryImageFile(Story story, MultipartFile file, String postfix) {
    super(
        STORY_IMAGE_BASE_PATH_FORMAT.formatted(story.getStoryKey()),
        file,
        postfix
    );
  }
}
