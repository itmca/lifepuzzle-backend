package io.itmca.aivideogenerator.domain.photo.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.itmca.aivideogenerator.domain.video.event.AiVideoCreateEvent;
import io.itmca.aivideogenerator.domain.video.service.VideoGenerationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiVideoCreateEventConsumer {

    private final VideoGenerationService videoGenerationService;
    private final ObjectMapper objectMapper;

    @StreamListener("aiVideoCreate-in-0")
    public void handleAiVideoCreate(@Payload String eventPayload) {
        try {
            log.info("Received AI video create event: {}", eventPayload);
            
            AiVideoCreateEvent event = objectMapper.readValue(eventPayload, AiVideoCreateEvent.class);
            
            videoGenerationService.processVideoGeneration(event);
            
            log.info("AI video create event processed successfully for heroId: {}, galleryId: {}, drivingVideoId: {}", 
                    event.getHeroId(), event.getGalleryId(), event.getDrivingVideoId());
            
        } catch (Exception e) {
            log.error("Failed to process AI video create event: {}", eventPayload, e);
            throw new RuntimeException("Failed to process AI video create event", e);
        }
    }
}