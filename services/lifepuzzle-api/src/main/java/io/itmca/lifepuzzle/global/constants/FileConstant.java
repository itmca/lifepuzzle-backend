package io.itmca.lifepuzzle.global.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileConstant {
  public static final String TEMP_FOLDER_PATH = "/tmp";

  public static final String STORY_BASE_PATH = "stories";
  public static final String ORIGINAL_BASE_PATH = "original";
  public static final String FILE_NAMES_SEPARATOR = "||";

  public static final String HERO_PROFILE_IMAGE_BASE_PATH_FORMAT = "heroes/profile/%s/images/";
  public static final String USER_PROFILE_IMAGE_BASE_PATH_FORMAT = "users/profile/%s/images/";
  public static final String STORY_IMAGE_BASE_PATH_FORMAT = "heroes/%s/images/original/";
  public static final String NEW_STORY_IMAGE_BASE_PATH_FORMAT = "heroes/%s/images/%s/original/";
  public static final String STORY_VIDEO_BASE_PATH_FORMAT = "heroes/%s/videos/original/";
  public static final String STORY_VOICE_BASE_PATH_FORMAT = "stories/%s/voices/original/";

  public static final int VIDEO_RESIZING_WIDTH = 854;
  public static final int VIDEO_RESIZING_HEIGHT = 480;

  public static final String FILE_DUPLICATE_PREFIX = "lp-media-";

  public static final int HERO_IMAGE_RESIZING_LONG_SIDE = 1080;
  public static final int HERO_IMAGE_RESIZING_SHORT_SIDE = 1080;
  public static final int STORY_IMAGE_RESIZING_THUMBNAIL_WIDTH = 240;
  public static final int STORY_IMAGE_RESIZING_GENERAL_WIDTH = 640;
  public static final int STORY_IMAGE_RESIZING_PINCH_ZOOM_WIDTH = 1280;
}
