package io.itmca.lifepuzzle.domain.story.endpoint;

import io.itmca.lifepuzzle.domain.auth.jwt.AuthPayload;
import io.itmca.lifepuzzle.domain.story.endpoint.request.StoryWriteRequest;
import io.itmca.lifepuzzle.domain.story.service.StoryWriteService;
import io.itmca.lifepuzzle.global.constant.FileConstant;
import io.itmca.lifepuzzle.global.infra.file.S3Repository;
import io.itmca.lifepuzzle.global.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class StoryWriteEndpoint {
    private final StoryWriteService storyWriteService;
    private final S3Repository s3Repository;

    @PostMapping(value = "/story")
    public void writeStory(@RequestPart("storyInfo") Optional<StoryWriteRequest> storyWriteRequest,
                           @RequestPart("photos") MultipartFile[] photos,
                           @RequestPart("voice") MultipartFile voice,
                           @AuthenticationPrincipal AuthPayload authPayload) throws IOException {

        var userNo = authPayload.getUserNo();

        if(!FileUtil.isExistFolder(FileConstant.TEMP_FOLDER_PATH)){
            FileUtil.createAllFolder(FileConstant.TEMP_FOLDER_PATH);
        }

        var photoFiles = FileUtil.getFilePaths(List.of(photos));
        var voiceFiles = FileUtil.getFilePaths(List.of(voice));
        var story =  storyWriteRequest.get().toStoryOf(userNo,
                String.join("||", photoFiles),
                String.join("||", voiceFiles));

        storyWriteService.saveStoryFiles(story, List.of(photos), List.of(voice));
        storyWriteService.create(story);
    }

}
