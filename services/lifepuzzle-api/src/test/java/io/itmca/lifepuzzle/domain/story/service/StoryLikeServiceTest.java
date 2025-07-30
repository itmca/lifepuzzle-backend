package io.itmca.lifepuzzle.domain.story.service;

import static io.itmca.lifepuzzle.domain.story.type.LikeType.STORY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.itmca.lifepuzzle.domain.story.entity.Like;
import io.itmca.lifepuzzle.domain.story.entity.Story;
import io.itmca.lifepuzzle.domain.story.repository.LikeRepository;
import io.itmca.lifepuzzle.global.exception.AlreadyLikedException;
import io.itmca.lifepuzzle.global.exception.LikeNotFoundException;
import io.itmca.lifepuzzle.global.exception.StoryNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class StoryLikeServiceTest {

  @Mock
  private StoryQueryService storyQueryService;

  @Mock
  private LikeRepository likeRepository;

  @InjectMocks
  private StoryLikeService storyLikeService;

  private final String storyKey = "storyKey";
  private final Long userId = 1L;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("스토리 좋아요 성공")
  public void addLikeSuccess() {
    // Given
    // 스토리가 존재하는 경우
    when(storyQueryService.findById(storyKey)).thenReturn(new Story());

    // 좋아요가 존재하지 않는 경우
    when(likeRepository.findLike(userId, storyKey, STORY)).thenReturn(Optional.empty());

    // When
    var response = storyLikeService.addLike(storyKey, userId);

    // Then
    assertTrue(response.getIsLiked());
    verify(storyQueryService).findById(storyKey);
    verify(likeRepository).save(any(Like.class));
  }

  @Test
  @DisplayName("스토리 좋아요 실패 - 이미 좋아요 한 경우")
  public void addLikeFailWhenAlreadyLiked() {
    // Given
    // 스토리가 존재하는 경우
    when(storyQueryService.findById(storyKey)).thenReturn(new Story());

    // 이미 존재하는 좋아요를 반환하도록 설정
    var existingLike = Like.builder()
        .userId(userId)
        .type(STORY)
        .targetId(storyKey)
        .build();
    when(likeRepository.findLike(userId, storyKey, STORY)).thenReturn(Optional.of(existingLike));

    // When & Then
    assertThrows(AlreadyLikedException.class, () -> {
      storyLikeService.addLike(storyKey, userId);
    });
  }

  @Test
  @DisplayName("스토리 좋아요 실패 - 스토리가 없는 경우")
  public void addLikeFailWhenStoryNotFound() {
    // Given
    // 스토리를 찾지 못하는 경우
    when(storyQueryService.findById(storyKey)).thenThrow(
        StoryNotFoundException.byStoryId(storyKey));

    // When & Then
    StoryNotFoundException exception = assertThrows(StoryNotFoundException.class, () -> {
      storyLikeService.addLike(storyKey, userId);
    });

    assertTrue(exception.getMessage().contains("storyId: " + storyKey));
  }

  @Test
  @DisplayName("스토리 좋아요 취소 성공")
  public void deleteLikeSuccess() {
    // Given
    // 스토리가 존재하는 경우
    when(storyQueryService.findById(storyKey)).thenReturn(new Story());

    // 존재하는 좋아요를 반환하도록 설정
    var existingLike = Like.builder()
        .userId(userId)
        .type(STORY)
        .targetId(storyKey)
        .build();
    when(likeRepository.findLike(userId, storyKey, STORY)).thenReturn(Optional.of(existingLike));

    // When
    var response = storyLikeService.deleteLike(storyKey, userId);

    // Then
    assertEquals(false, response.getIsLiked());
    verify(likeRepository).delete(existingLike);
  }

  @Test
  @DisplayName("스토리 좋아요 취소 실패 - 좋아요가 없는 경우")
  public void deleteLikeFailWhenLikeNotFound() {
    // Given
    // 스토리가 존재하는 경우
    when(storyQueryService.findById(storyKey)).thenReturn(new Story());

    // 좋아요가 존재하지 않는 경우
    when(likeRepository.findLike(userId, storyKey, STORY)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(LikeNotFoundException.class, () -> {
      storyLikeService.deleteLike(storyKey, userId);
    });
  }

  @Test
  @DisplayName("스토리 좋아요 취소 실패 - 스토리가 없는 경우")
  public void deleteLikeFailWhenStoryNotFound() {
    // Given
    // 스토리를 찾지 못하는 경우
    when(storyQueryService.findById(storyKey)).thenThrow(
        StoryNotFoundException.byStoryId(storyKey));

    // When & Then
    StoryNotFoundException exception = assertThrows(StoryNotFoundException.class, () -> {
      storyLikeService.deleteLike(storyKey, userId);
    });

    assertTrue(exception.getMessage().contains("storyId: " + storyKey));
  }
}