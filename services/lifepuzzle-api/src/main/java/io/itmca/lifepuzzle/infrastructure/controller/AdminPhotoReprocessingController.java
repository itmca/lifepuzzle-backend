package io.itmca.lifepuzzle.infrastructure.controller;

import io.itmca.lifepuzzle.domain.content.entity.Gallery;
import io.itmca.lifepuzzle.domain.content.repository.GalleryRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin/photo-reprocessing")
public class AdminPhotoReprocessingController {

  @Autowired
  private GalleryRepository galleryRepository;

  @Autowired
  private StreamBridge streamBridge;

  @Value("${spring.cloud.stream.bindings.imageResizeRequest-out-0.destination:image-resize-queue}")
  private String queueDestination;

  @GetMapping("/status")
  public ResponseEntity<Map<String, Object>> getReprocessingStatus() {
    Map<String, Object> status = new HashMap<>();

    // 전체 이미지 수
    long totalImages = galleryRepository.count();

    // 리사이징이 필요한 이미지 수 (resizedSizes가 비어있거나 3개 미만)
    List<Gallery> needsReprocessing = galleryRepository.findAll().stream()
        .filter(gallery -> gallery.isImage())
        .filter(gallery -> gallery.getResizedSizes().size() < 3)
        .toList();

    status.put("timestamp", LocalDateTime.now());
    status.put("totalImages", totalImages);
    status.put("needsReprocessing", needsReprocessing.size());
    status.put("queueDestination", queueDestination);

    return ResponseEntity.ok(status);
  }

  @PostMapping("/reprocess-missing-sizes")
  public ResponseEntity<Map<String, Object>> reprocessMissingSizes(
      @RequestParam(defaultValue = "50") int batchSize,
      @RequestParam(defaultValue = "1000") long delayMs) {

    log.info("Starting photo reprocessing for missing sizes - batchSize: {}, delayMs: {}",
        batchSize, delayMs);

    // 리사이징이 필요한 이미지들 조회
    List<Gallery> needsReprocessing = galleryRepository.findAll().stream()
        .filter(gallery -> gallery.isImage())
        .filter(gallery -> gallery.getResizedSizes().size() < 3)
        .toList();

    log.info("Found {} images that need reprocessing", needsReprocessing.size());

    int processedCount = 0;
    int successCount = 0;
    int errorCount = 0;

    // 배치로 처리
    for (int i = 0; i < needsReprocessing.size(); i += batchSize) {
      int endIndex = Math.min(i + batchSize, needsReprocessing.size());
      List<Gallery> batch = needsReprocessing.subList(i, endIndex);

      log.info("Processing batch {}-{} of {}", i + 1, endIndex, needsReprocessing.size());

      for (Gallery gallery : batch) {
        try {
          // RabbitMQ에 메시지 발송
          Map<String, Object> message = Map.of("id", gallery.getId());
          boolean sent = streamBridge.send(queueDestination, message);

          if (sent) {
            successCount++;
            log.debug("Sent reprocessing message for photo ID: {}", gallery.getId());
          } else {
            errorCount++;
            log.error("Failed to send reprocessing message for photo ID: {}", gallery.getId());
          }

          processedCount++;

        } catch (Exception e) {
          errorCount++;
          log.error("Error sending reprocessing message for photo ID: {}", gallery.getId(), e);
        }
      }

      // 배치 간 지연
      if (i + batchSize < needsReprocessing.size()) {
        try {
          Thread.sleep(delayMs);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          log.warn("Batch processing interrupted");
          break;
        }
      }
    }

    Map<String, Object> result = new HashMap<>();
    result.put("timestamp", LocalDateTime.now());
    result.put("totalFound", needsReprocessing.size());
    result.put("processed", processedCount);
    result.put("successful", successCount);
    result.put("errors", errorCount);
    result.put("batchSize", batchSize);
    result.put("delayMs", delayMs);

    log.info("Photo reprocessing completed - processed: {}, successful: {}, errors: {}",
        processedCount, successCount, errorCount);

    return ResponseEntity.ok(result);
  }

  @PostMapping("/reprocess-all")
  public ResponseEntity<Map<String, Object>> reprocessAllPhotos(
      @RequestParam(defaultValue = "50") int batchSize,
      @RequestParam(defaultValue = "1000") long delayMs,
      @RequestParam(required = false) Long startId,
      @RequestParam(required = false) Long endId) {

    log.info("Starting full photo reprocessing - batchSize: {}, delayMs: {}, startId: {}, endId: {}",
        batchSize, delayMs, startId, endId);

    // 모든 이미지 조회 (범위 지정 가능)
    List<Gallery> allImages;
    if (startId != null || endId != null) {
      allImages = galleryRepository.findAll().stream()
          .filter(gallery -> gallery.isImage())
          .filter(gallery -> startId == null || gallery.getId() >= startId)
          .filter(gallery -> endId == null || gallery.getId() <= endId)
          .toList();
    } else {
      allImages = galleryRepository.findAll().stream()
          .filter(gallery -> gallery.isImage())
          .toList();
    }

    log.info("Found {} images to reprocess", allImages.size());

    int processedCount = 0;
    int successCount = 0;
    int errorCount = 0;

    // 배치로 처리
    for (int i = 0; i < allImages.size(); i += batchSize) {
      int endIndex = Math.min(i + batchSize, allImages.size());
      List<Gallery> batch = allImages.subList(i, endIndex);

      log.info("Processing batch {}-{} of {}", i + 1, endIndex, allImages.size());

      for (Gallery gallery : batch) {
        try {
          // RabbitMQ에 메시지 발송
          Map<String, Object> message = Map.of("id", gallery.getId());
          boolean sent = streamBridge.send(queueDestination, message);

          if (sent) {
            successCount++;
            log.debug("Sent reprocessing message for photo ID: {}", gallery.getId());
          } else {
            errorCount++;
            log.error("Failed to send reprocessing message for photo ID: {}", gallery.getId());
          }

          processedCount++;

        } catch (Exception e) {
          errorCount++;
          log.error("Error sending reprocessing message for photo ID: {}", gallery.getId(), e);
        }
      }

      // 배치 간 지연
      if (i + batchSize < allImages.size()) {
        try {
          Thread.sleep(delayMs);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          log.warn("Batch processing interrupted");
          break;
        }
      }
    }

    Map<String, Object> result = new HashMap<>();
    result.put("timestamp", LocalDateTime.now());
    result.put("totalFound", allImages.size());
    result.put("processed", processedCount);
    result.put("successful", successCount);
    result.put("errors", errorCount);
    result.put("batchSize", batchSize);
    result.put("delayMs", delayMs);
    result.put("startId", startId);
    result.put("endId", endId);

    log.info("Full photo reprocessing completed - processed: {}, successful: {}, errors: {}",
        processedCount, successCount, errorCount);

    return ResponseEntity.ok(result);
  }
}