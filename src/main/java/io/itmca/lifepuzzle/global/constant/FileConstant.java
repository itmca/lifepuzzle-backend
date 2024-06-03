package io.itmca.lifepuzzle.global.constant;

import java.io.File;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileConstant {
  public static final String TEMP_FOLDER_PATH =
      String.format("%s%stmp", System.getProperty("user.dir"), File.separator);

  public static final String HERO_PROFILE_BASE_PATH = "hero/profile/%s/image/";
  public static final String USER_PROFILE_BASE_PATH = "user/profile/%s/image/";
  public static final String STORY_BASE_PATH = "stories";
  public static final String FILE_NAMES_SEPARATOR = "||";

  public static final String USER_PROFILE_DEFAULT_IMAGE_PATH = "user/profile/default.jpeg";
  public static final String HERO_PROFILE_DEFAULT_IMAGE_PATH = "hero/profile/default.jpeg";
  public static final String HERO_PROFILE_IMAGE_BASE_PATH_FORMAT = "hero/profile/%s/image/";
  public static final String USER_PROFILE_IMAGE_BASE_PATH_FORMAT = "user/profile/%s/image/";
  public static final String STORY_IMAGE_BASE_PATH_FORMAT = "stories/%s/image/";
  public static final String STORY_VIDEO_BASE_PATH_FORMAT = "stories/%s/video/";
  public static final String STORY_VOICE_BASE_PATH_FORMAT = "stories/%s/voice/";

  public static final int VIDEO_RESIZING_WIDTH = 854;
  public static final int VIDEO_RESIZING_HEIGHT = 480;

  public static final String FILE_DUPLICATE_PREFIX = "lp-media-";

  public static final int HERO_IMAGE_RESIZING_WIDTH = 128;
  public static final int HERO_IMAGE_RESIZING_HEIGHT = 128;
  public static final int STORY_IMAGE_RESIZING_WIDTH = 640;
  public static final int STORY_IMAGE_RESIZING_HEIGHT = 480;
}
