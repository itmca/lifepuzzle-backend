package io.itmca.lifepuzzle.domain.story.service;

import static io.itmca.lifepuzzle.global.constant.FileConstant.STORY_BASE_PATH;
import static io.itmca.lifepuzzle.global.util.StreamUtil.toStream;
import static java.io.File.separator;
import static org.springframework.util.CollectionUtils.isEmpty;

import io.itmca.lifepuzzle.domain.story.entity.Story;
import io.itmca.lifepuzzle.domain.story.file.StoryFile;
import io.itmca.lifepuzzle.domain.story.file.StoryImageFile;
import io.itmca.lifepuzzle.domain.story.file.StoryVideoFile;
import io.itmca.lifepuzzle.domain.story.file.StoryVoiceFile;
import io.itmca.lifepuzzle.domain.story.repository.StoryRepository;
import io.itmca.lifepuzzle.global.infra.file.CustomFile;
import io.itmca.lifepuzzle.global.infra.file.service.S3UploadService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoryWriteService {
  private final StoryRepository storyRepository;
  private final S3UploadService s3UploadService;

  public Story create(Story story, StoryFile storyFile) {
    uploadStoryFile(storyFile);

    story.addStoryFile(storyFile);

    return storyRepository.save(story);
  }

  private void uploadStoryFile(StoryFile storyFile) {
    if (!isEmpty(storyFile.images())) {
      s3UploadService.upload(storyFile.images());
    }

    if (!isEmpty(storyFile.voices())) {
      s3UploadService.upload(storyFile.voices());
    }

    if (!isEmpty(storyFile.videos())) {
      s3UploadService.upload(storyFile.videos());
    }
  }

  @Transactional
  public void update(Story story, StoryFile storyFile) {
    deleteFileFromS3(story, storyFile);

    uploadStoryFile(getStoryFileToUpload(story, storyFile));

    story.addStoryFile(storyFile);
  }

  private StoryFile getStoryFileToUpload(Story story, StoryFile storyFile) {
    var images =
        (List<StoryImageFile>) getNonDuplicateFiles(storyFile.images(), story.getImageNames());

    var voices =
        (List<StoryVoiceFile>) getNonDuplicateFiles(storyFile.voices(), story.getAudios());

    var videos =
        (List<StoryVideoFile>) getNonDuplicateFiles(storyFile.videos(), story.getVideoNames());

    return StoryFile.builder()
        .images(images)
        .voices(voices)
        .videos(videos)
        .build();
  }

  private List<? extends CustomFile> getNonDuplicateFiles(List<? extends CustomFile> files,
                                                          List<String> existingFileNames) {
    return toStream(files)
        .filter(file -> !existingFileNames.contains(file.getFileName()))
        .toList();
  }

  private void deleteFileFromS3(Story story, StoryFile storyFile) {
    var imageNames =
        getFileNamesToDelete(storyFile.images(), story.getImageNames());
    if (!isEmpty(imageNames)) {
      s3UploadService.delete(story.getImageFolder(), imageNames);
    }

    var voiceNames =
        getFileNamesToDelete(storyFile.voices(), story.getAudioNames());
    if (!isEmpty(voiceNames)) {
      s3UploadService.delete(story.getAudioFolder(), voiceNames);
    }

    var videoNames =
        getFileNamesToDelete(storyFile.videos(), story.getVideoNames());
    if (!isEmpty(videoNames)) {
      s3UploadService.delete(story.getVideoFolder(), videoNames);
    }
  }

  private static List<String> getFileNamesToDelete(List<? extends CustomFile> files,
                                                   List<String> existingFileNames) {
    var requestFileNames = getRequestFileNames(files);

    return toStream(existingFileNames)
        .filter(name -> !requestFileNames.contains(name))
        .toList();
  }

  private static List<String> getRequestFileNames(List<? extends CustomFile> files) {
    return toStream(files)
        .map(file -> file.getFileName())
        .toList();
  }

  public void delete(String storyKey) {
    s3UploadService.delete(String.join(separator, STORY_BASE_PATH, storyKey));

    storyRepository.deleteById(storyKey);
  }
}
