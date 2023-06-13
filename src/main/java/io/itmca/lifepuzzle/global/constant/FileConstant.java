package io.itmca.lifepuzzle.global.constant;

import java.io.File;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileConstant {
  public static final String TEMP_FOLDER_PATH =
      String.format("%s%stmp", System.getProperty("user.dir"), File.separator);

  public static final String HERO_PROFILE_BASE_PATH = "hero/profile";
  public static final String STORY_BASE_PATH = "stories";
  public static final String FILE_NAMES_SEPARATOR = "||";
}
