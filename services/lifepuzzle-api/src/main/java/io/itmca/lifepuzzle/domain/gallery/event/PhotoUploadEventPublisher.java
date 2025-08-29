package io.itmca.lifepuzzle.domain.gallery.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhotoUploadEventPublisher {
  private static final String PHOTO_UPLOAD_OUT_BINDING = "photoUpload-out-0";
  private static final String ROUTING_KEY = "photo.resizing.request";
  
  private final StreamBridge streamBridge;
  
  public void publishPhotoUploadEvent(Long photoId, Long heroId, String url) {
    try {
      PhotoUploadEvent event = PhotoUploadEvent.of(photoId, heroId, url);
      
      var message = MessageBuilder.withPayload(event)
          .setHeader("routingKey", ROUTING_KEY)
          .build();
          
      streamBridge.send(PHOTO_UPLOAD_OUT_BINDING, message);
      
      log.info("Published photo upload event for photoId: {}, heroId: {}", photoId, heroId);
    } catch (Exception e) {
      log.error("Failed to publish photo upload event for photoId: {}, heroId: {}", 
                photoId, heroId, e);
      // Don't throw exception to avoid breaking the main photo upload flow
    }
  }
}