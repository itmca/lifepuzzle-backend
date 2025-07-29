package io.itmca.lifepuzzle.domain.gallery.service;

import static io.itmca.lifepuzzle.domain.story.type.GalleryType.IMAGE;
import static io.itmca.lifepuzzle.domain.story.type.GalleryType.VIDEO;

import io.itmca.lifepuzzle.domain.gallery.repository.GalleryRepository;
import io.itmca.lifepuzzle.domain.story.entity.Gallery;
import io.itmca.lifepuzzle.domain.story.file.StoryImageFile;
import io.itmca.lifepuzzle.domain.story.file.StoryVideoFile;
import io.itmca.lifepuzzle.domain.story.type.AgeGroup;
import io.itmca.lifepuzzle.global.exception.GalleryItemNotFoundException;
import io.itmca.lifepuzzle.global.file.service.S3UploadService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GalleryWriteService {
  private final GalleryRepository galleryRepository;
  private final S3UploadService s3UploadService;
  private final GalleryAsyncService galleryAsyncService;

  @Transactional
  public void saveGallery(Long heroId, List<MultipartFile> gallery, AgeGroup ageGroup) {
    List<StoryImageFile> storyImageFiles = StoryImageFile.listFrom(gallery, heroId);
    List<StoryVideoFile> storyVideoFiles = StoryVideoFile.listFrom(gallery, heroId);

    List<Gallery> saveGalleryFiles = new ArrayList<>();

    saveGalleryFiles.addAll(Gallery.listFrom(storyImageFiles, heroId, ageGroup, IMAGE));
    saveGalleryFiles.addAll(Gallery.listFrom(storyVideoFiles, heroId, ageGroup, VIDEO));

    s3UploadService.upload(storyImageFiles);
    s3UploadService.upload(storyVideoFiles);

    galleryRepository.saveAllAndFlush(saveGalleryFiles);

    galleryAsyncService.saveResizeGalleryAsync(
        getImageIdsWithStoryGallery(saveGalleryFiles, storyImageFiles));
  }

  private Map<Long, StoryImageFile> getImageIdsWithStoryGallery(
      List<Gallery> saveGalleryFiles, List<StoryImageFile> storyImageFiles) {
    Map<Long, StoryImageFile> storyImageFileMap = new HashMap<>();
    for (StoryImageFile image : storyImageFiles) {
      Optional<Gallery> targetGallery = saveGalleryFiles.stream().filter(
          item -> item.isImage()
              && item.getUrl().equals(image.getBase() + image.getFileName())
      ).findFirst();

      targetGallery.ifPresent(storyPhoto -> storyImageFileMap.put(storyPhoto.getId(), image));
    }
    return storyImageFileMap;
  }

  @Transactional
  public void deleteGalleryItem(Long galleryId) {
    Gallery gallery = galleryRepository.findById(galleryId).orElse(null);
    if (gallery == null) {
      throw GalleryItemNotFoundException.of(galleryId);
    }
    s3UploadService.delete(gallery.getUrl());
    galleryRepository.delete(gallery);
  }

  public Optional<List<?>> test() {
    return Optional.empty();
  }
}
