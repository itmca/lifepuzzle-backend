package io.itmca.lifepuzzle.domain.story.endpoint;

import io.itmca.lifepuzzle.domain.auth.jwt.AuthPayload;
import io.itmca.lifepuzzle.domain.story.endpoint.request.StoryWriteRequest;
import io.itmca.lifepuzzle.domain.story.service.StoryWriteService;
import io.itmca.lifepuzzle.global.util.FileUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.ArrayList;
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
                         @RequestPart(value = "photos", required = false) MultipartFile[] photos,
                         @RequestPart(value = "voice", required = false) MultipartFile[] voice,
                         @AuthenticationPrincipal AuthPayload authPayload) throws IOException {

    var userNo = authPayload.getUserNo();
    var photoList = photos != null ? List.of(photos) : new ArrayList<MultipartFile>();
    var voiceList = voice != null ? List.of(voice) : new ArrayList<MultipartFile>();
    var photoFiles = FileUtil.getFilePaths(photoList);
    var voiceFiles = FileUtil.getFilePaths(voiceList);
    var story = storyWriteRequest.toStory(userNo,
        String.join("||", photoFiles),
        String.join("||", voiceFiles));

    storyWriteService.saveStoryFiles(story, photoList, voiceList);
    storyWriteService.create(story);
  }

}
