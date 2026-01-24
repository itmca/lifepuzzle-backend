package io.itmca.lifepuzzle.domain.ai.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiVideoCreateEventPublisher {
  private final StreamBridge streamBridge;

  private static final String AI_VIDEO_CREATE_OUT_BINDING = "aiVideoCreate-out-0";
  private static final String ROUTING_KEY = "gallery.ai.video.create";

  public void publishPhotoUploadEvent(Long heroNo, Long galleryId, Long drivingVideoId) {
    try {
      var payload = java.util.Map
          .of("heroId", heroNo, "galleryId", galleryId, "drivingVideoId", drivingVideoId);

      var message = MessageBuilder.withPayload(payload)
          .setHeader("routingKey", ROUTING_KEY)
          .build();

      streamBridge.send(AI_VIDEO_CREATE_OUT_BINDING, message);

      log.info("Published ai video create event for heroId: {}, galleryId: {}, drivingVideo: {}",
          heroNo, galleryId, drivingVideoId);
    } catch (Exception e) {
      log.error("Failed to ai video create event for heroId: {}, galleryId: {}, drivingVideo: {}",
          heroNo, galleryId, drivingVideoId, e);
      // Don't throw exception to avoid breaking the ai video create flow
    }
  }
}
