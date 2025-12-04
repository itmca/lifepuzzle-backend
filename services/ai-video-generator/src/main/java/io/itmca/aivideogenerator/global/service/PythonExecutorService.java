package io.itmca.aivideogenerator.global.service;

import io.itmca.aivideogenerator.domain.drivingvideo.repository.AiDrivingVideoRepository;
import io.itmca.aivideogenerator.domain.gallery.repository.GalleryRepository;
import io.itmca.aivideogenerator.domain.video.entity.AiGeneratedVideo;
import io.itmca.aivideogenerator.domain.video.repository.AiGeneratedVideoRepository;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PythonExecutorService {

  private final GalleryRepository galleryRepository;
  private final AiDrivingVideoRepository aiDrivingVideoRepository;
  private final AiGeneratedVideoRepository aiGeneratedVideoRepository;
  private final S3UploadService s3UploadService;
  
  @Value("${ai.video.output.path:/var/tmp/ai_video/}")
  private String outputPath;
  
  @Value("${ai.video.conda.env:LivePortrait}")
  private String condaEnv;
  
  @Value("${ai.video.download.path:/var/tmp/ai_video_download/}")
  private String downloadPath;
  
  @Value("${ai.video.project.path:/Users/jeong/lifepuzzle/external/LivePortrait}")
  private String projectPath;

  public void prepareVideoGeneration(AiGeneratedVideo video) {
    log.info("Preparing Python command execution for video generation: {}", video.getId());

    try {
      // 1. 갤러리 정보로 이미지 URL 조회
      var gallery = galleryRepository.findById(video.getGalleryId())
          .orElseThrow(() -> new RuntimeException("Gallery not found: " + video.getGalleryId()));
      
      // 2. 드라이빙 비디오 정보 조회
      var drivingVideo = aiDrivingVideoRepository.findActiveById(video.getDrivingVideoId())
          .orElseThrow(() -> new RuntimeException("Driving video not found: " + video.getDrivingVideoId()));
      
      // 3. 파일 다운로드 및 로컬 경로 획득
      String galleryFilePath = downloadFileFromUrl(gallery.getUrl(), "gallery_" + video.getGalleryId());
      String drivingVideoFilePath = getDrivingVideoFilePath(drivingVideo.getUrl(), video.getDrivingVideoId());
      
      // 4. Python 명령어 실행
      String command = buildPythonCommand(galleryFilePath, drivingVideoFilePath);
      log.info("Executing Python command: {}", command);
      
      // 5. Python 명령어 실행
      executePythonCommand(command, video);
      
      // 6. 생성된 비디오 파일 처리
      processGeneratedVideo(video);
      
    } catch (Exception e) {
      log.error("Failed to prepare video generation for video: {}", video.getId(), e);
      video.markAsFailed(e.getMessage());
      aiGeneratedVideoRepository.save(video);
      throw new RuntimeException("Video generation preparation failed", e);
    }

    log.info("Python command execution preparation completed for video: {}", video.getId());
  }

  private String downloadFileFromUrl(String url, String fileName) throws IOException {
    // 다운로드 디렉토리 생성
    Path downloadDir = Paths.get(downloadPath);
    Files.createDirectories(downloadDir);
    
    // 파일 확장자 추출
    String extension = getFileExtensionFromUrl(url);
    String fullFileName = fileName + extension;
    Path filePath = downloadDir.resolve(fullFileName);
    
    // 파일이 이미 존재하면 경로만 반환
    if (Files.exists(filePath)) {
      log.info("File already exists, skipping download: {}", filePath);
      return filePath.toString();
    }
    
    log.info("Downloading file from URL: {} to {}", url, filePath);
    
    try (InputStream in = URI.create(url).toURL().openStream();
         FileOutputStream out = new FileOutputStream(filePath.toFile())) {
      
      byte[] buffer = new byte[8192];
      int bytesRead;
      while ((bytesRead = in.read(buffer)) != -1) {
        out.write(buffer, 0, bytesRead);
      }
    }
    
    log.info("File downloaded successfully: {}", filePath);
    return filePath.toString();
  }
  
  private String getDrivingVideoFilePath(String url, Long drivingVideoId) throws IOException {
    String fileName = "driving_video_" + drivingVideoId;
    
    // 파일 확장자 추출
    String extension = getFileExtensionFromUrl(url);
    String fullFileName = fileName + extension;
    Path filePath = Paths.get(downloadPath, fullFileName);
    
    // 드라이빙 비디오는 이미 존재하면 다운로드 안 함
    if (Files.exists(filePath)) {
      log.info("Driving video file already exists, skipping download: {}", filePath);
      return filePath.toString();
    }
    
    // 존재하지 않으면 다운로드
    return downloadFileFromUrl(url, fileName);
  }
  
  private String getFileExtensionFromUrl(String url) {
    String fileName = url.substring(url.lastIndexOf('/') + 1);
    int dotIndex = fileName.lastIndexOf('.');
    if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
      return fileName.substring(dotIndex);
    }
    // 확장자가 없으면 기본값 설정 (컨텐츠 타입에 따라)
    if (url.contains("video") || url.contains(".mp4") || url.contains(".avi")) {
      return ".mp4";
    }
    return ".jpg"; // 기본 이미지 확장자
  }

  private String buildPythonCommand(String galleryFilePath, String drivingVideoFilePath) {
    return String.format("cd %s && source ~/.bashrc && conda activate %s && PYTORCH_ENABLE_MPS_FALLBACK=1 python inference.py -d %s -s %s -o %s", 
        projectPath, condaEnv, drivingVideoFilePath, galleryFilePath, outputPath);
  }

  private void executePythonCommand(String command, AiGeneratedVideo video) {
    log.info("Executing Python command: {} for video: {}", command, video.getId());

    try {
      ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
      processBuilder.redirectErrorStream(true);
      
      Process process = processBuilder.start();
      
      // 실시간 로그 출력
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
        String line;
        while ((line = reader.readLine()) != null) {
          log.info("Python output: {}", line);
        }
      }
      
      // 프로세스 완료 대기 (최대 30분)
      boolean finished = process.waitFor(30, TimeUnit.MINUTES);
      
      if (!finished) {
        process.destroyForcibly();
        throw new RuntimeException("Python command timed out after 30 minutes");
      }
      
      int exitCode = process.exitValue();
      if (exitCode != 0) {
        throw new RuntimeException("Python command failed with exit code: " + exitCode);
      }
      
      log.info("Python command completed successfully for video: {}", video.getId());

    } catch (Exception e) {
      log.error("Failed to execute Python command for video: {}", video.getId(), e);
      throw new RuntimeException("Python command execution failed", e);
    }
  }
  
  private void processGeneratedVideo(AiGeneratedVideo video) {
    try {
      // 생성된 비디오 파일 찾기
      Path outputDir = Paths.get(outputPath);
      File[] videoFiles = outputDir.toFile().listFiles((dir, name) -> 
          name.toLowerCase().endsWith(".mp4") || name.toLowerCase().endsWith(".avi"));
      
      if (videoFiles == null || videoFiles.length == 0) {
        throw new RuntimeException("No video file found in output directory: " + outputPath);
      }
      
      // 가장 최근에 생성된 파일 선택
      File latestVideoFile = null;
      long lastModified = 0;
      for (File file : videoFiles) {
        if (file.lastModified() > lastModified) {
          lastModified = file.lastModified();
          latestVideoFile = file;
        }
      }
      
      if (latestVideoFile == null) {
        throw new RuntimeException("No valid video file found");
      }
      
      log.info("Found generated video file: {}", latestVideoFile.getAbsolutePath());
      
      // S3에 업로드
      String s3Url = s3UploadService.uploadVideoFile(latestVideoFile, 
          video.getHeroNo(), video.getGalleryId(), video.getDrivingVideoId());
      
      // DB 업데이트
      video.markAsCompleted(s3Url);
      aiGeneratedVideoRepository.save(video);
      
      // 로컬 파일 삭제
      try {
        Files.delete(latestVideoFile.toPath());
        log.info("Deleted local video file: {}", latestVideoFile.getAbsolutePath());
      } catch (Exception e) {
        log.warn("Failed to delete local video file: {}", latestVideoFile.getAbsolutePath(), e);
      }
      
      log.info("Video processing completed successfully. S3 URL: {}", s3Url);
      
    } catch (Exception e) {
      log.error("Failed to process generated video for video: {}", video.getId(), e);
      video.markAsFailed("Video processing failed: " + e.getMessage());
      aiGeneratedVideoRepository.save(video);
      throw new RuntimeException("Video processing failed", e);
    }
  }
}