package io.itmca.lifepuzzle.domain.story.endpoint;

import static io.itmca.lifepuzzle.global.util.StreamUtil.toStream;

import io.itmca.lifepuzzle.domain.auth.jwt.AuthPayload;
import io.itmca.lifepuzzle.domain.story.endpoint.request.StoryWriteRequest;
import io.itmca.lifepuzzle.domain.story.file.StoryFile;
import io.itmca.lifepuzzle.domain.story.file.StoryImageFile;
import io.itmca.lifepuzzle.domain.story.file.StoryVoiceFile;
import io.itmca.lifepuzzle.domain.story.service.StoryWriteService;
import io.itmca.lifepuzzle.global.infra.file.service.S3UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
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
  private final S3UploadService s3UploadService;

  @Operation(summary = "스토리 등록")
  @PostMapping(value = "/story")
  public void writeStory(@RequestPart("storyInfo") StoryWriteRequest storyWriteRequest,
                         @RequestPart(value = "photos", required = false)
                         List<MultipartFile> images,
                         @RequestPart(value = "voice", required = false)
                         List<MultipartFile> voices,
                         @AuthenticationPrincipal AuthPayload authPayload) throws IOException {

    var story = storyWriteRequest.toStory(authPayload.getUserNo());
    var storyFile = StoryFile.builder()
        .images(toStream(images)
            .map(image -> new StoryImageFile(story, image))
            .toList())
        .voices(toStream(voices)
            .map(voice -> new StoryVoiceFile(story, voice))
            .toList())
        .build();

    storyWriteService.create(story, storyFile);
  }
}
