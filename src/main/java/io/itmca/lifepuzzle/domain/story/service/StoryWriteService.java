package io.itmca.lifepuzzle.domain.story.service;

import io.itmca.lifepuzzle.domain.story.entity.Story;
import io.itmca.lifepuzzle.domain.story.repository.StoryRepository;
import io.itmca.lifepuzzle.global.constant.FileConstant;
import io.itmca.lifepuzzle.global.infra.file.S3Repository;
import io.itmca.lifepuzzle.global.util.FileUtil;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class StoryWriteService {
  private final StoryRepository storyRepository;
  private final S3Repository s3Repository;

  public Story create(Story story) {
    return storyRepository.save(story);
  }

  ;

  public void update(Story story) {
    storyRepository.save(story);
  }

  ;

  public void saveStoryFiles(Story story, List<MultipartFile> photos, List<MultipartFile> voices)
      throws IOException {
    var photoFolder = story.getImageFolder();
    var imageFileNames = story.getImageFiles();
    var photoFileNames = StringUtils.hasText(imageFileNames)
        ? List.of(imageFileNames.split("\\|\\|"))
        : Collections.<String>emptyList();
    var voiceFolder = story.getAudioFolder();
    var audioFileNames = story.getAudioFiles();
    var voiceFileNames = StringUtils.hasText(audioFileNames)
        ? List.of(audioFileNames.split("\\|\\|"))
        : Collections.<String>emptyList();

    if (!FileUtil.isExistFolder(FileConstant.TEMP_FOLDER_PATH)) {
      FileUtil.createAllFolder(FileConstant.TEMP_FOLDER_PATH);
    }

    saveFiles(photoFolder, photoFileNames, photos);
    saveFiles(voiceFolder, voiceFileNames, voices);
  }

  ;

  private void saveFiles(String folderPath, List<String> filePaths, List<MultipartFile> files)
      throws IOException {
    var len = files.size();
    System.out.println(len);

    for (var i = 0; i < len; i++) {
      var targetMultiPartFile = files.get(i);
      var tempFilePath =
          String.format(FileConstant.TEMP_FOLDER_PATH + File.separator + filePaths.get(i));
      var saveS3FilePath = String.format("%s/%s", folderPath, filePaths.get(i));

      var localFile = FileUtil.saveMultiPartFileInLocal(targetMultiPartFile, tempFilePath);

      s3Repository.upload(localFile, saveS3FilePath);

      FileUtil.deleteLocalFile(localFile);
    }
  }
}
