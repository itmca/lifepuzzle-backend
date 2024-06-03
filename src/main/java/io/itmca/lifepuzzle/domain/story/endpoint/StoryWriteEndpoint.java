package io.itmca.lifepuzzle.domain.story.endpoint;

import static io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus.ADMIN;
import static io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus.OWNER;
import static io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus.WRITER;
import static io.itmca.lifepuzzle.global.util.FileUtil.handleSameNameContents;

import io.itmca.lifepuzzle.domain.auth.jwt.AuthPayload;
import io.itmca.lifepuzzle.domain.story.endpoint.request.StoryWriteRequest;
import io.itmca.lifepuzzle.domain.story.endpoint.response.StoryWriteResponse;
import io.itmca.lifepuzzle.domain.story.file.StoryFile;
import io.itmca.lifepuzzle.domain.story.file.StoryImageFile;
import io.itmca.lifepuzzle.domain.story.file.StoryVideoFile;
import io.itmca.lifepuzzle.domain.story.file.StoryVoiceFile;
import io.itmca.lifepuzzle.domain.story.service.StoryQueryService;
import io.itmca.lifepuzzle.domain.story.service.StoryWriteService;
import io.itmca.lifepuzzle.domain.user.service.UserQueryService;
import io.itmca.lifepuzzle.global.aop.AuthCheck;
import io.itmca.lifepuzzle.global.aop.HeroNoContainer;
import io.itmca.lifepuzzle.global.exception.HeroNotAccessibleToStoryException;
import io.itmca.lifepuzzle.global.exception.UserNotAccessibleToStoryException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
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

  @AuthCheck(auths = { WRITER, ADMIN, OWNER })
  @Operation(summary = "스토리 등록")
  @PostMapping(value = "/story")
  public ResponseEntity<StoryWriteResponse> writeStory(
      @RequestPart("storyInfo") @HeroNoContainer StoryWriteRequest storyWriteRequest,
      @RequestPart(value = "photos", required = false)
      List<MultipartFile> images,
      @RequestPart(value = "voice", required = false)
      List<MultipartFile> voices,
      @RequestPart(value = "videos", required = false)
      List<MultipartFile> videos,
      @AuthenticationPrincipal AuthPayload authPayload) throws IOException {

    var story = storyWriteRequest.toStory(authPayload.getUserNo());

    var storyFile = StoryFile.builder()
        .images(handleSameNameContents(
            images,
            (image) -> new StoryImageFile(story, image).resize(),
            (image, postfix) -> new StoryImageFile(story, image, postfix).resize())
        )
        .voices(handleSameNameContents(
            voices,
            (voice) -> new StoryVoiceFile(story, voice),
            (voice, postfix) -> new StoryVoiceFile(story, voice, postfix))
        )
        .videos(handleSameNameContents(
            videos,
            (video) -> new StoryVideoFile(story, video).resize(),
            (video, postfix) -> new StoryVideoFile(story, video, postfix).resize())
        )
        .build();

    storyWriteService.create(story, storyFile);

    return ResponseEntity.ok(
        StoryWriteResponse.builder()
            .storyKey(story.getStoryKey())
            .build()
    );
  }

  @Operation(summary = "스토리 수정")
  @PutMapping(value = "/story/{storyKey}")
  public void updateStory(@PathVariable("storyKey") String storyKey,
                          @RequestPart("storyInfo") StoryWriteRequest storyWriteRequest,
                          @RequestPart(value = "photos", required = false)
                          List<MultipartFile> images,
                          @RequestPart(value = "voice", required = false)
                          List<MultipartFile> voices,
                          @RequestPart(value = "videos", required = false)
                          List<MultipartFile> videos,
                          @AuthenticationPrincipal AuthPayload authPayload) throws IOException {

    var story = storyQueryService.findById(storyKey);

    if (story.getUserNo() != authPayload.getUserNo()) {
      throw new UserNotAccessibleToStoryException(authPayload.getUserNo(), storyKey);
    }

    // storyWriteRequest에 있는 heroNo로 비교하는 것보다,
    // authPayload에 있는 userNo로 heroNo를 조회하여 비교하는 게 맞을까요?
    if (story.getHeroNo() != storyWriteRequest.getHeroNo()) {
      throw new HeroNotAccessibleToStoryException(storyWriteRequest.getHeroNo(), storyKey);
    }

    story.updateStoryInfo(storyWriteRequest);

    // TODO 2023.09.09 Solmioh 이름 중복일 때 처리 필요. 주온이 사진만 임시코드만 추가 해 놓음
    var storyFile = StoryFile.builder()
        .images(handleSameNameContents(
            images,
            (image) -> new StoryImageFile(story, image).resize(),
            (image, postfix) -> new StoryImageFile(story, image, postfix).resize())
        )
        .voices(handleSameNameContents(
            voices,
            (voice) -> new StoryVoiceFile(story, voice),
            (voice, postfix) -> new StoryVoiceFile(story, voice, postfix))
        )
        .videos(handleSameNameContents(
            videos,
            (video) -> new StoryVideoFile(story, video).resize(),
            (video, postfix) -> new StoryVideoFile(story, video, postfix).resize())
        )
        .build();

    storyWriteService.update(story, storyFile);
  }

  @Operation(summary = "스토리 삭제")
  @DeleteMapping(value = "/story/{storyKey}")
  public HttpStatus deleteStory(@PathVariable("storyKey") String storyKey,
                                @AuthenticationPrincipal AuthPayload authPayload) {

    var story = storyQueryService.findById(storyKey);

    if (story.getUserNo() != authPayload.getUserNo()) {
      throw new UserNotAccessibleToStoryException(authPayload.getUserNo(), storyKey);
    }

    var user = userQueryService.findByUserNo(authPayload.getUserNo());
    if (story.getHeroNo() != user.getRecentHeroNo()) {
      throw new HeroNotAccessibleToStoryException(user.getRecentHeroNo(), storyKey);
    }

    storyWriteService.delete(storyKey);

    return HttpStatus.OK;
  }
}