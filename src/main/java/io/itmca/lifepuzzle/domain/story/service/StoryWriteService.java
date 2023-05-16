package io.itmca.lifepuzzle.domain.story.service;

import io.itmca.lifepuzzle.domain.story.entity.Story;
import io.itmca.lifepuzzle.domain.story.repository.StoryRepository;
import io.itmca.lifepuzzle.global.constant.FileConstant;
import io.itmca.lifepuzzle.global.infra.file.S3Repository;
import io.itmca.lifepuzzle.global.util.FileUtil;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
    var photoFileNames = Arrays.stream(story.getImageFiles().split("\\|\\|")).toList();
    var voiceFolder = story.getAudioFolder();
    var voiceFileNames = Arrays.stream(story.getAudioFiles().split("\\|\\|")).toList();

    if (!FileUtil.isExistFolder(FileConstant.TEMP_FOLDER_PATH)) {
      FileUtil.createAllFolder(FileConstant.TEMP_FOLDER_PATH);
    }

    saveFiles(photoFolder, photoFileNames, photos);
    saveFiles(voiceFolder, voiceFileNames, voices);
  }

  ;

  private void saveFiles(String folderPath, List<String> filePaths, List<MultipartFile> files)
      throws IOException {
    var len = filePaths.size();

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
