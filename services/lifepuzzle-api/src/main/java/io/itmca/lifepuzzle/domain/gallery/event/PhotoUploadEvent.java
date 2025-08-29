package io.itmca.lifepuzzle.domain.gallery.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PhotoUploadEvent {
  @JsonProperty("id")
  private final Long photoId;
  
  @JsonProperty("hero_id")
  private final Long heroId;
  
  @JsonProperty("url")
  private final String url;
  
  @JsonProperty("uploaded_at")
  private final String uploadedAt;
  
  public static PhotoUploadEvent of(Long photoId, Long heroId, String url) {
    return PhotoUploadEvent.builder()
        .photoId(photoId)
        .heroId(heroId)
        .url(url)
        .uploadedAt(java.time.Instant.now().toString())
        .build();
  }
}