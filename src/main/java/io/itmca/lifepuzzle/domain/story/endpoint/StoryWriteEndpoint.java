package io.itmca.lifepuzzle.domain.story.endpoint;

import static io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus.ADMIN;
import static io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus.OWNER;
import static io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus.WRITER;

import io.itmca.lifepuzzle.domain.auth.jwt.AuthPayload;
import io.itmca.lifepuzzle.domain.story.endpoint.request.StoryGalleryWriteRequest;
import io.itmca.lifepuzzle.domain.story.repository.StoryRepository;
import io.itmca.lifepuzzle.domain.story.service.StoryQueryService;
import io.itmca.lifepuzzle.domain.story.service.StoryWriteService;
import io.itmca.lifepuzzle.domain.user.service.UserQueryService;
import io.itmca.lifepuzzle.global.aop.AuthCheck;
import io.itmca.lifepuzzle.global.aop.HeroNo;
import io.itmca.lifepuzzle.global.exception.HeroNotAccessibleToStoryException;
import io.itmca.lifepuzzle.global.exception.UserNotAccessibleToStoryException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Tag(name = "스토리 등록")
public class StoryWriteEndpoint {
  private final StoryWriteService storyWriteService;
  private final StoryQueryService storyQueryService;
  private final UserQueryService userQueryService;
  private final StoryRepository storyRepository;

  @Deprecated
  @AuthCheck(auths = {WRITER, ADMIN, OWNER})
  @Operation(summary = "스토리 등록")
  @PostMapping(value = {"/v2/heroes/{heroId}/stories",
      "/v1/heroes/{heroId}/stories"})
  public ResponseEntity<Void> createStory(
      @HeroNo @PathVariable("heroId") Long heroId,
      @RequestPart(value = "story") StoryGalleryWriteRequest storyGalleryWriteRequest,
      @RequestPart(value = "voice", required = false) MultipartFile voice,
      @AuthenticationPrincipal AuthPayload authPayload) {
    var story = storyGalleryWriteRequest.toStory(heroId, authPayload.getUserId());

    storyWriteService.create(story, storyGalleryWriteRequest.getGalleryIds(), voice);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  //@AuthCheck(auths = {WRITER, ADMIN, OWNER})
  @Operation(summary = "스토리 등록")
  @PostMapping({"/v3/galleries/stories"})
  public ResponseEntity<Void> createStoryV2(
      @RequestPart(value = "story") StoryGalleryWriteRequest storyGalleryWriteRequest,
      @RequestPart(value = "voice", required = false) MultipartFile voice,
      @AuthenticationPrincipal AuthPayload authPayload) {
    var story = storyGalleryWriteRequest.toStory(authPayload.getUserId());

    storyWriteService.create(story, storyGalleryWriteRequest.getGalleryIds(), voice);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Deprecated
  @AuthCheck(auths = {WRITER, ADMIN, OWNER})
  @Operation(summary = "스토리 수정")
  @PutMapping(value = {"/v2/heroes/{heroId}/stories/{storyId}",
      "/v1/heroes/{heroId}/stories/{storyId}"})
  public ResponseEntity<Void> putStory(
      @HeroNo @PathVariable("heroId") Long heroId,
      @PathVariable("storyId") String storyId,
      @RequestPart(value = "story") StoryGalleryWriteRequest storyGalleryWriteRequest,
      @RequestPart(value = "voice", required = false) MultipartFile voice,
      @AuthenticationPrincipal AuthPayload authPayload) {
    storyWriteService.update(storyId, storyGalleryWriteRequest, voice);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  //@AuthCheck(auths = {WRITER, ADMIN, OWNER})
  @Operation(summary = "스토리 수정")
  @PutMapping({"/v3/galleries/stories/{storyId}"})
  public ResponseEntity<Void> putStoryV2(
      @PathVariable("storyId") String storyId,
      @RequestPart(value = "story") StoryGalleryWriteRequest storyGalleryWriteRequest,
      @RequestPart(value = "voice", required = false) MultipartFile voice) {
    storyWriteService.update(storyId, storyGalleryWriteRequest, voice);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Operation(summary = "스토리 삭제")
  @DeleteMapping({"/v1/stories/{storyKey}", // TODO: FE 전환 후 제거
                  "/v2/galleries/stories/{storyKey}"})
  public void deleteStory(@PathVariable("storyKey") String storyKey,
                          @AuthenticationPrincipal AuthPayload authPayload) {
    var story = storyQueryService.findById(storyKey);

    if (story.getUserId() != authPayload.getUserId()) {
      throw new UserNotAccessibleToStoryException(authPayload.getUserId(), storyKey);
    }

    var user = userQueryService.findByUserNo(authPayload.getUserId());
    if (story.getHeroId() != user.getRecentHeroNo()) {
      throw new HeroNotAccessibleToStoryException(user.getRecentHeroNo(), storyKey);
    }

    storyRepository.delete(story);
  }
}