package io.itmca.lifepuzzle.domain.story.endpoint;

import static io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus.ADMIN;
import static io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus.OWNER;
import static io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus.WRITER;
import static io.itmca.lifepuzzle.global.util.FileUtil.handleSameNameContents;

import io.itmca.lifepuzzle.domain.auth.jwt.AuthPayload;
import io.itmca.lifepuzzle.domain.story.endpoint.request.GalleryWriteRequest;
import io.itmca.lifepuzzle.domain.story.endpoint.request.StoryGalleryWriteRequest;
import io.itmca.lifepuzzle.domain.story.endpoint.request.StoryWriteRequest;
import io.itmca.lifepuzzle.domain.story.endpoint.response.StoryWriteResponse;
import io.itmca.lifepuzzle.domain.story.entity.Story;
import io.itmca.lifepuzzle.domain.story.file.StoryFile;
import io.itmca.lifepuzzle.domain.story.file.StoryImageFile;
import io.itmca.lifepuzzle.domain.story.file.StoryVideoFile;
import io.itmca.lifepuzzle.domain.story.file.StoryVoiceFile;
import io.itmca.lifepuzzle.domain.story.service.StoryPhotoService;
import io.itmca.lifepuzzle.domain.story.service.StoryQueryService;
import io.itmca.lifepuzzle.domain.story.service.StoryWriteService;
import io.itmca.lifepuzzle.domain.user.service.UserQueryService;
import io.itmca.lifepuzzle.global.ai.chat.OpenAiChatService;
import io.itmca.lifepuzzle.global.ai.stt.SpeechToTextService;
import io.itmca.lifepuzzle.global.aop.AuthCheck;
import io.itmca.lifepuzzle.global.aop.HeroNoContainer;
import io.itmca.lifepuzzle.global.exception.HeroNotAccessibleToStoryException;
import io.itmca.lifepuzzle.global.exception.UserNotAccessibleToStoryException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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
  private final SpeechToTextService speechToTextService;
  private final OpenAiChatService openAiChatService;
  private final StoryPhotoService storyPhotoService;

  @Deprecated
  @AuthCheck(auths = {WRITER, ADMIN, OWNER})
  @Operation(summary = "스토리 등록")
  @PostMapping({"/story", // TODO: FE 전환 후 제거
      "/stories"})
  public ResponseEntity<StoryWriteResponse> writeStory(
      @RequestPart("storyInfo") @HeroNoContainer StoryWriteRequest storyWriteRequest,
      @RequestPart(value = "gallery", required = false)
      List<MultipartFile> gallery,
      @RequestPart(value = "photos", required = false)
      List<MultipartFile> images,
      @RequestPart(value = "voice", required = false)
      List<MultipartFile> voices,
      @RequestPart(value = "videos", required = false)
      List<MultipartFile> videos,
      @AuthenticationPrincipal AuthPayload authPayload) throws IOException {

    var story = storyWriteRequest.toStory(authPayload.getUserId());

    var storyFile = CollectionUtils.isEmpty(gallery)
        ? buildStoryFile(images, voices, videos, story)
        : buildStoryFileWithGallery(gallery, voices, story);

    storyWriteService.create(story, storyFile);
    storyPhotoService.savePhotos(story, storyFile);

    return ResponseEntity.ok(
        StoryWriteResponse.builder()
            .storyKey(story.getId())
            .build()
    );
  }

  @AuthCheck(auths = {WRITER, ADMIN, OWNER})
  @Operation(summary = "스토리 등록")
  @PostMapping("/v2/heroes/{heroId}/stories")
  public ResponseEntity<Void> createStory(
      @PathVariable("heroId") Long heroId,
      @RequestBody @Valid StoryGalleryWriteRequest storyGalleryWriteRequest,
      @AuthenticationPrincipal AuthPayload authPayload) {
    var story = storyGalleryWriteRequest.toStory(heroId, authPayload.getUserId());

    storyWriteService.create(story, storyGalleryWriteRequest.getGalleryIds());
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  // TODO: FE에서 사진 분리 작업이 끝나면 제거
  @Deprecated
  private static StoryFile buildStoryFileWithGallery(List<MultipartFile> gallery,
                                                     List<MultipartFile> voices,
                                                     Story story) {
    return StoryFile.builder()
        .images(handleSameNameContents(
            gallery.stream().filter(
                    file -> file.getContentType() != null && file.getContentType().startsWith("image"))
                .toList(),
            // TODO: resize() 메서드 제거했는데 resize 정책 다시 확인 후 추가 필요
            (image) -> new StoryImageFile(story.getHeroId(), image),
            (image, postfix) -> new StoryImageFile(story.getHeroId(), image, postfix).resize())
        )
        .voices(handleSameNameContents(
            voices,
            (voice) -> new StoryVoiceFile(story, voice),
            (voice, postfix) -> new StoryVoiceFile(story, voice, postfix))
        )
        .videos(handleSameNameContents(
            gallery.stream().filter(
                    file -> file.getContentType() != null && !file.getContentType().startsWith("image"))
                .toList(),
            // TODO: resize() 메서드 제거했는데 resize 정책 다시 확인 후 추가 필요
            (video) -> new StoryVideoFile(story.getHeroId(), video),
            (video, postfix) -> new StoryVideoFile(story.getHeroId(), video, postfix))
        ).build();
  }

  // TODO: FE에서 gallery로 전환 후 제거
  @Deprecated
  private static StoryFile buildStoryFile(List<MultipartFile> images, List<MultipartFile> voices,
                                          List<MultipartFile> videos, Story story) {
    return StoryFile.builder()
        .images(handleSameNameContents(
            images,
            (image) -> new StoryImageFile(story.getHeroId(), image).resize(),
            (image, postfix) -> new StoryImageFile(story.getHeroId(), image, postfix).resize())
        )
        .voices(handleSameNameContents(
            voices,
            (voice) -> new StoryVoiceFile(story, voice),
            (voice, postfix) -> new StoryVoiceFile(story, voice, postfix))
        )
        .videos(handleSameNameContents(
            videos,
            (video) -> new StoryVideoFile(story.getHeroId(), video).resize(),
            (video, postfix) -> new StoryVideoFile(story.getHeroId(), video, postfix))
        )
        .build();
  }

  @Operation(summary = "스토리 수정")
  @PutMapping({"/story/{storyKey}", // TODO: FE 전환 후 제거
      "/stories/{storyKey}"})
  public void updateStory(@PathVariable("storyKey") String storyKey,
                          @RequestPart("storyInfo") StoryWriteRequest storyWriteRequest,
                          @RequestPart(value = "gallery", required = false)
                          List<MultipartFile> gallery,
                          @RequestPart(value = "photos", required = false)
                          List<MultipartFile> images,
                          @RequestPart(value = "voice", required = false)
                          List<MultipartFile> voices,
                          @RequestPart(value = "videos", required = false)
                          List<MultipartFile> videos,
                          @AuthenticationPrincipal AuthPayload authPayload) throws IOException {

    var story = storyQueryService.findById(storyKey);

    if (story.getUserId() != authPayload.getUserId()) {
      throw new UserNotAccessibleToStoryException(authPayload.getUserId(), storyKey);
    }

    // storyWriteRequest에 있는 heroNo로 비교하는 것보다,
    // authPayload에 있는 userNo로 heroNo를 조회하여 비교하는 게 맞을까요?
    if (story.getHeroId() != storyWriteRequest.getHeroNo()) {
      throw new HeroNotAccessibleToStoryException(storyWriteRequest.getHeroNo(), storyKey);
    }

    story.updateStoryInfo(storyWriteRequest);

    var storyFile = CollectionUtils.isEmpty(gallery)
        ? buildStoryFile(images, voices, videos, story)
        : buildStoryFileWithGallery(gallery, voices, story);

    storyPhotoService.updatePhotos(story, storyFile);
    storyWriteService.update(story, storyFile);
  }

  @Operation(summary = "스토리 삭제")
  @DeleteMapping({"/story/{storyKey}", // TODO: FE 전환 후 제거
      "/stories/{storyKey}"})
  public HttpStatus deleteStory(@PathVariable("storyKey") String storyKey,
                                @AuthenticationPrincipal AuthPayload authPayload) {

    var story = storyQueryService.findById(storyKey);

    if (story.getUserId() != authPayload.getUserId()) {
      throw new UserNotAccessibleToStoryException(authPayload.getUserId(), storyKey);
    }

    var user = userQueryService.findByUserNo(authPayload.getUserId());
    if (story.getHeroId() != user.getRecentHeroNo()) {
      throw new HeroNotAccessibleToStoryException(user.getRecentHeroNo(), storyKey);
    }

    storyPhotoService.deletePhotos(story);
    storyWriteService.delete(storyKey);

    return HttpStatus.OK;
  }

  @PostMapping({"/stories/speech-to-text"})
  public String convertSpeechToText(
      @RequestPart(value = "voice", required = false)
      List<MultipartFile> voices,
      @RequestParam(value = "isTest", defaultValue = "false", required = false)
      boolean isTest
  ) {
    if (isTest) {
      return String.join("\\n",
          "동해물과 백두산이 마르고 닳도록 하느님이 보우하사 우리나라 만세 무궁화 삼천리 화려강산 대한사람 대한으로 길이 보전하세",
          "떳다 떳다 비행기 날아라 날아라 높이 높이 날아라",
          "떳다 떳다 비행기 시원하게 시원하게 날아라 날아라 시원하게 날아라");
    }


    StoryVoiceFile storyVoiceFile = new StoryVoiceFile(new Story(), voices.get(0));
    String convertTextToStt = speechToTextService.transcribeAudio(storyVoiceFile);
    return openAiChatService.requestChat("맞춤법 및 오타 교정해서 내용만 보여줘", convertTextToStt);
  }

  @PostMapping({"v1/heroes/gallery"})
  public void savePhoto(
      @RequestPart List<MultipartFile> gallery,
      @RequestPart(value = "galleryInfo") GalleryWriteRequest galleryWriteRequest) {
    storyPhotoService.saveGallery(galleryWriteRequest.getHeroId(),
        gallery, galleryWriteRequest.getAgeGroup());
  }

  @DeleteMapping({"v1/heroes/gallery/{galleryId}"})
  public void deleteGalleryItem(@PathVariable Long galleryId) {
    storyPhotoService.deleteGalleryItem(galleryId);
  }
}