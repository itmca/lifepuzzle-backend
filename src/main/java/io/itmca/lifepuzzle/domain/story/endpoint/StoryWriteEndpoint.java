package io.itmca.lifepuzzle.domain.story.endpoint;

import io.itmca.lifepuzzle.domain.auth.jwt.AuthPayload;
import io.itmca.lifepuzzle.domain.story.endpoint.request.StoryWriteRequest;
import io.itmca.lifepuzzle.domain.story.service.StoryWriteService;
import io.itmca.lifepuzzle.global.infra.file.ImageCustomFile;
import io.itmca.lifepuzzle.global.infra.file.VoiceCustomFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Tag(name = "스토리 등록")
public class StoryWriteEndpoint {
  private final StoryWriteService storyWriteService;

  @Operation(summary = "스토리 등록")
  @PostMapping(value = "/story")
  public void writeStory(@RequestPart("storyInfo") StoryWriteRequest storyWriteRequest,
                         @RequestPart(value = "photos", required = false)
                         List<MultipartFile> multiImages,
                         @RequestPart(value = "voice", required = false)
                         List<MultipartFile> multiVoices,
                         @AuthenticationPrincipal AuthPayload authPayload) throws IOException {
    var story = storyWriteRequest.toStory(authPayload.getUserNo());
    var images = multiImages == null
        ? Collections.EMPTY_LIST
        : multiImages.stream()
        .map(photo -> new ImageCustomFile(photo))
        .toList();
    var voices = multiVoices == null
        ? Collections.EMPTY_LIST
        : multiVoices.stream()
        .map(voice -> new VoiceCustomFile(voice))
        .toList();

    storyWriteService.saveFile(story, images);
    storyWriteService.saveFile(story, voices);

    storyWriteService.create(story);
  }
}
