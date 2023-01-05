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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

        System.out.println(storyWriteRequest.get());
        var userNo = authPayload.getUserNo();
        var photoFiles = new ArrayList<String>();
        var voiceFiles = new ArrayList<String>();

        if(!FileUtil.isExistFolder(FileConstant.TEMP_FOLDER_PATH)){
            FileUtil.createAllFolder(FileConstant.TEMP_FOLDER_PATH);
        }

        for(var photo : photos) {
            var saveFileName = Math.round(Math.random() * 1000000) + photo.getOriginalFilename();

            photoFiles.add(saveFileName);

            var file = new File(FileConstant.TEMP_FOLDER_PATH + File.separator + saveFileName);

            photo.transferTo(file);
            //s3Repository.upload(file);

            file.delete();
        }

        var saveFileName = Math.round(Math.random() * 1000000) + "_" + voice.getOriginalFilename();

        voiceFiles.add(saveFileName);

        var file = new File(FileConstant.TEMP_FOLDER_PATH + File.separator + saveFileName);

        voice.transferTo(file);
        //s3Repository.upload(file);

        file.delete();

        var story =  storyWriteRequest.get().toStoryOf(userNo,
                String.join("||", photoFiles),
                String.join("||", voiceFiles));

        storyWriteService.create(story);
    }

}
