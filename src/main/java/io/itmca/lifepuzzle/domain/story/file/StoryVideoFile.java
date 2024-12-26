package io.itmca.lifepuzzle.domain.story.file;

import static io.itmca.lifepuzzle.global.constant.FileConstant.STORY_VIDEO_BASE_PATH_FORMAT;
import static io.itmca.lifepuzzle.global.constant.FileConstant.VIDEO_RESIZING_HEIGHT;
import static io.itmca.lifepuzzle.global.constant.FileConstant.VIDEO_RESIZING_WIDTH;
import static io.itmca.lifepuzzle.global.util.FileUtil.handleSameNameContents;

import io.github.techgnious.IVCompressor;
import io.github.techgnious.dto.IVSize;
import io.github.techgnious.dto.VideoFormats;
import io.github.techgnious.exception.VideoException;
import io.itmca.lifepuzzle.global.infra.file.CustomFile;
import io.itmca.lifepuzzle.global.infra.file.Resizable;
import io.itmca.lifepuzzle.global.util.FileUtil;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class StoryVideoFile extends CustomFile implements Resizable<StoryVideoFile> {

  public StoryVideoFile(Long heroId, MultipartFile file) {
    this(heroId, file, "");
  }

  public StoryVideoFile(Long heroId, MultipartFile file, String postfix) {
    super(
        STORY_VIDEO_BASE_PATH_FORMAT.formatted(heroId),
        file,
        postfix
    );
  }

  @Builder
  private StoryVideoFile(StoryVideoFile storyVideoFile, byte[] bytes) {
    super(storyVideoFile, bytes);
  }

  @Override
  public StoryVideoFile resize() {
    var customRes = new IVSize();
    customRes.setWidth(VIDEO_RESIZING_WIDTH);
    customRes.setHeight(VIDEO_RESIZING_HEIGHT);

    try {
      var resizedVideo =
          new IVCompressor().reduceVideoSizeWithCustomRes(bytes, VideoFormats.MP4, customRes);

      return StoryVideoFile.builder()
          .storyVideoFile(this)
          .bytes(resizedVideo)
          .build();
    } catch (VideoException e) {
      throw new RuntimeException(e);
    }
  }

  public static List<StoryVideoFile> listFrom(List<MultipartFile> gallery, Long heroId) {
    return handleSameNameContents(
        gallery.stream().filter(file -> !FileUtil.isImageFile(file)).toList(),
        // TODO: resize() 메서드 제거했는데 resize 정책 다시 확인 후 추가 필요
        (video) -> new StoryVideoFile(heroId, video),
        (video, postfix) -> new StoryVideoFile(heroId, video, postfix));
  }
}