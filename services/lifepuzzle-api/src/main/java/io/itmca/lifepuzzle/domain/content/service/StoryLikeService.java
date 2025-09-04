package io.itmca.lifepuzzle.domain.content.service;

import static io.itmca.lifepuzzle.domain.content.type.LikeType.STORY;

import io.itmca.lifepuzzle.domain.content.endpoint.response.LikeWriteResponse;
import io.itmca.lifepuzzle.domain.content.entity.Like;
import io.itmca.lifepuzzle.domain.content.repository.LikeRepository;
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
