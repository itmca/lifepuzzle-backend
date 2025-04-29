package io.itmca.lifepuzzle.domain.story.service;

import static io.itmca.lifepuzzle.global.constants.FileConstant.STORY_IMAGE_RESIZING_LIST_WIDTH;
import static io.itmca.lifepuzzle.global.constants.FileConstant.STORY_IMAGE_RESIZING_PINCH_ZOOM_WIDTH;
import static io.itmca.lifepuzzle.global.constants.FileConstant.STORY_IMAGE_RESIZING_THUMBNAIL_WIDTH;

import io.itmca.lifepuzzle.domain.story.entity.StoryPhoto;
import io.itmca.lifepuzzle.domain.story.file.StoryImageFile;
import io.itmca.lifepuzzle.domain.story.repository.StoryPhotoRepository;
import io.itmca.lifepuzzle.global.exception.GalleryItemNotFoundException;
import io.itmca.lifepuzzle.global.file.CustomFile;
import io.itmca.lifepuzzle.global.file.service.S3UploadService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoryPhotoAsyncService {
  private final StoryPhotoRepository storyPhotoRepository;
  private final S3UploadService s3UploadService;

  @Async
  @Transactional
  public void saveResizeGalleryAsync(Map<Long, StoryImageFile> storyImageFiles) {
    List<CustomFile> s3UploadStoryImageFiles = new ArrayList<>();
    List<StoryPhoto> updateGallery = new ArrayList<>();

    storyImageFiles.forEach((galleryId, file) -> {
      var gallery = storyPhotoRepository.findById(galleryId)
          .orElseThrow(() -> GalleryItemNotFoundException.of(galleryId));

      var targetResizeSizes = Arrays.asList(STORY_IMAGE_RESIZING_THUMBNAIL_WIDTH,
          STORY_IMAGE_RESIZING_LIST_WIDTH, STORY_IMAGE_RESIZING_PINCH_ZOOM_WIDTH);
      var resizedSizes = new ArrayList<Integer>();

      for (Integer targetResizeSize : targetResizeSizes) {
        file.resize(targetResizeSize).ifPresent(
            resizeImage -> {
              resizedSizes.add(targetResizeSize);
              s3UploadStoryImageFiles.add(resizeImage);
            }
        );
      }

      if (!resizedSizes.isEmpty()) {
        gallery.addResizedSizes(resizedSizes);
        updateGallery.add(gallery);
      }
    });

    s3UploadService.upload(s3UploadStoryImageFiles);
    storyPhotoRepository.saveAll(updateGallery);
  }
}
