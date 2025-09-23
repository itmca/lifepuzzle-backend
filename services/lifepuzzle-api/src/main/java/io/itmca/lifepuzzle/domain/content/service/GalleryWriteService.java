package io.itmca.lifepuzzle.domain.content.service;

import static io.itmca.lifepuzzle.domain.content.type.GalleryType.IMAGE;
import static io.itmca.lifepuzzle.domain.content.type.GalleryType.VIDEO;

import io.itmca.lifepuzzle.domain.content.endpoint.response.GalleryUploadCompleteResponse;
import io.itmca.lifepuzzle.domain.content.endpoint.response.dto.GalleryUploadResultDto;
import io.itmca.lifepuzzle.domain.content.entity.Gallery;
import io.itmca.lifepuzzle.domain.content.event.PhotoUploadEventPublisher;
import io.itmca.lifepuzzle.domain.content.repository.GalleryRepository;
import io.itmca.lifepuzzle.domain.content.type.AgeGroup;
import io.itmca.lifepuzzle.domain.content.type.GalleryStatus;
import io.itmca.lifepuzzle.global.exception.GalleryItemNotFoundException;
import io.itmca.lifepuzzle.global.file.domain.StoryImageFile;
import io.itmca.lifepuzzle.global.file.domain.StoryVideoFile;
import io.itmca.lifepuzzle.global.file.service.S3UploadService;
import java.util.ArrayList;
import java.util.List;
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
  private final PhotoUploadEventPublisher photoUploadEventPublisher;

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

    // Publish photo upload events to RabbitMQ for image resizing
    publishPhotoUploadEvents(saveGalleryFiles, heroId);
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

  private void publishPhotoUploadEvents(List<Gallery> galleries, Long heroId) {
    galleries.stream()
        .filter(Gallery::isImage) // Only publish events for images, not videos
        .forEach(gallery -> {
          photoUploadEventPublisher.publishPhotoUploadEvent(
              gallery.getId(),
              heroId,
              gallery.getUrl()
          );
        });
  }

  @Transactional
  public GalleryUploadCompleteResponse completeUploads(List<String> fileKeys) {
    List<GalleryUploadResultDto> results = new ArrayList<>();
    int successCount = 0;
    int failureCount = 0;

    for (String fileKey : fileKeys) {
      Optional<Gallery> galleryOpt = galleryRepository.findByUrl(fileKey);
      
      if (galleryOpt.isEmpty()) {
        results.add(new GalleryUploadResultDto(
            fileKey, 
            null, 
            GalleryStatus.FAILED, 
            "Gallery not found for file key"
        ));
        failureCount++;
        continue;
      }

      Gallery gallery = galleryOpt.get();
      
      try {
        gallery.setGalleryStatus(GalleryStatus.UPLOADED);
        galleryRepository.save(gallery);
        
        results.add(new GalleryUploadResultDto(
            fileKey,
            gallery.getId(),
            GalleryStatus.UPLOADED,
            "Upload completed successfully"
        ));
        successCount++;
        
        if (gallery.isImage()) {
          photoUploadEventPublisher.publishPhotoUploadEvent(
              gallery.getId(),
              gallery.getHeroId(),
              gallery.getUrl()
          );
        }
        
      } catch (Exception e) {
        gallery.setGalleryStatus(GalleryStatus.FAILED);
        galleryRepository.save(gallery);
        
        results.add(new GalleryUploadResultDto(
            fileKey,
            gallery.getId(),
            GalleryStatus.FAILED,
            "Failed to update status: " + e.getMessage()
        ));
        failureCount++;
      }
    }

    return new GalleryUploadCompleteResponse(results, successCount, failureCount);
  }

  public Optional<List<?>> test() {
    return Optional.empty();
  }
}
