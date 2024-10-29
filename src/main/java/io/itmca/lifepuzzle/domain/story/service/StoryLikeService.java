package io.itmca.lifepuzzle.domain.story.service;

import static io.itmca.lifepuzzle.domain.story.LikeType.STORY;

import io.itmca.lifepuzzle.domain.story.endpoint.response.LikeWriteResponse;
import io.itmca.lifepuzzle.domain.story.entity.Like;
import io.itmca.lifepuzzle.domain.story.repository.LikeRepository;
import io.itmca.lifepuzzle.global.exception.AlreadyLikedException;
import io.itmca.lifepuzzle.global.exception.LikeNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoryLikeService {

  private final StoryQueryService storyQueryService;
  private final LikeRepository likeRepository;

  @Transactional
  public LikeWriteResponse addLike(String storyKey, Long userId) {
    storyQueryService.findById(storyKey);

    likeRepository.findLike(userId, storyKey, STORY)
        .ifPresentOrElse(
            like -> {
              throw AlreadyLikedException.forStory(storyKey);
            },
            () -> {
              likeRepository.save(
                  Like.builder()
                      .userId(userId)
                      .type(STORY)
                      .targetId(storyKey)
                      .build()
              );
            }
        );

    return new LikeWriteResponse(true);
  }

  @Transactional
  public LikeWriteResponse deleteLike(String storyKey, Long userId) {
    storyQueryService.findById(storyKey);

    likeRepository.findLike(userId, storyKey, STORY)
        .ifPresentOrElse(
            likeRepository::delete,
            () -> {
              throw new LikeNotFoundException(storyKey);
            }
        );

    return new LikeWriteResponse(false);
  }
}
