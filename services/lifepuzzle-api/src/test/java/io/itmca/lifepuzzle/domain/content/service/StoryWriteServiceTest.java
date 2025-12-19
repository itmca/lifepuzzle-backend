package io.itmca.lifepuzzle.domain.content.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.itmca.lifepuzzle.domain.content.endpoint.request.StoryContentUploadRequest;
import io.itmca.lifepuzzle.domain.content.endpoint.request.StoryVoiceUploadRequest;
import io.itmca.lifepuzzle.domain.content.entity.Gallery;
import io.itmca.lifepuzzle.domain.content.entity.Story;
import io.itmca.lifepuzzle.domain.content.entity.StoryGallery;
import io.itmca.lifepuzzle.domain.content.repository.GalleryRepository;
import io.itmca.lifepuzzle.domain.content.repository.StoryGalleryRepository;
import io.itmca.lifepuzzle.domain.content.repository.StoryRepository;
import io.itmca.lifepuzzle.domain.content.type.AgeGroup;
import io.itmca.lifepuzzle.domain.content.type.GalleryStatus;
import io.itmca.lifepuzzle.domain.content.type.GalleryType;
import io.itmca.lifepuzzle.global.file.domain.StoryVoiceFile;
import io.itmca.lifepuzzle.global.file.service.S3UploadService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class StoryWriteServiceTest {

  @Mock
  private StoryRepository storyRepository;
  @Mock
  private GalleryRepository galleryRepository;
  @Mock
  private S3UploadService s3UploadService;
  @Mock
  private StoryGalleryRepository storyGalleryRepository;
  @Mock
  private MultipartFile voiceFile;

  @InjectMocks
  private StoryWriteService storyWriteService;

  private final Long heroId = 1L;
  private final Long galleryId = 10L;
  private final Long userId = 20L;

  private Gallery gallery;

  @BeforeEach
  void setUp() {
    gallery = Gallery.builder()
        .id(galleryId)
        .heroId(heroId)
        .url("path/to/file")
        .ageGroup(AgeGroup.UNDER_TEENAGER)
        .galleryType(GalleryType.IMAGE)
        .galleryStatus(GalleryStatus.UPLOADED)
        .build();
  }

  @Test
  void upsertContent_createsStoryWhenMissing() {
    var request = new StoryContentUploadRequest(heroId, galleryId, "new story content");
    when(storyRepository.findByHeroIdAndGalleryId(heroId, galleryId)).thenReturn(Optional.empty());
    when(galleryRepository.findByIdAndHeroId(galleryId, heroId)).thenReturn(Optional.of(gallery));
    when(storyRepository.save(any(Story.class))).thenAnswer(invocation -> invocation.getArgument(0));

    var storyId = storyWriteService.upsertContent(request, userId);

    assertThat(storyId).isNotBlank();
    verify(storyGalleryRepository).save(argThat(
        map -> map.getGalleryId().equals(galleryId) && map.getStoryId() != null));
    verify(s3UploadService, never()).upload(any(StoryVoiceFile.class));
  }

  @Test
  void upsertContent_updatesExistingStory() {
    var request = new StoryContentUploadRequest(heroId, galleryId, "updated content");
    var existingStory = Story.builder()
        .id("story-key")
        .heroId(heroId)
        .userId(userId)
        .content("old content")
        .build();
    when(storyRepository.findByHeroIdAndGalleryId(heroId, galleryId))
        .thenReturn(Optional.of(existingStory));

    storyWriteService.upsertContent(request, userId);

    assertThat(existingStory.getContent()).isEqualTo("updated content");
    verify(storyRepository, never()).save(any(Story.class));
    verify(storyGalleryRepository, never()).save(any(StoryGallery.class));
  }

  @Test
  void upsertVoice_uploadsVoiceAndCreatesStoryIfMissing() throws Exception {
    var request = new StoryVoiceUploadRequest(heroId, galleryId);
    when(storyRepository.findByHeroIdAndGalleryId(heroId, galleryId)).thenReturn(Optional.empty());
    when(galleryRepository.findByIdAndHeroId(galleryId, heroId)).thenReturn(Optional.of(gallery));
    when(storyRepository.save(any(Story.class))).thenAnswer(invocation -> invocation.getArgument(0));

    when(voiceFile.getOriginalFilename()).thenReturn("voice.mp3");
    when(voiceFile.getBytes()).thenReturn("voice".getBytes());
    when(voiceFile.getContentType()).thenReturn("audio/mpeg");

    var storyId = storyWriteService.upsertVoice(request, voiceFile, userId);

    assertThat(storyId).isNotBlank();
    verify(s3UploadService).upload(any(StoryVoiceFile.class));
    verify(storyGalleryRepository).save(any(StoryGallery.class));
  }
}
