package io.itmca.lifepuzzle.domain.content.file;

import static io.itmca.lifepuzzle.global.constants.FileConstant.STORY_VOICE_BASE_PATH_FORMAT;

import io.itmca.lifepuzzle.domain.content.entity.Story;
import io.itmca.lifepuzzle.global.file.CustomFile;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class StoryVoiceFile extends CustomFile {
  public StoryVoiceFile(Story story, MultipartFile file) {
    this(story, file, "");
  }

  public StoryVoiceFile(Story story, MultipartFile file, String postfix) {
    super(
        STORY_VOICE_BASE_PATH_FORMAT.formatted(story.getId()),
        file,
        postfix
    );
  }
}
