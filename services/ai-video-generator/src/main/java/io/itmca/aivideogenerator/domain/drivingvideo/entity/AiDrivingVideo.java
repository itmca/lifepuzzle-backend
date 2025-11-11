package io.itmca.aivideogenerator.domain.drivingvideo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Table(name = "ai_driving_video")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AiDrivingVideo {
  @Id
  private Long id;
  
  @Column(nullable = false, length = 100)
  private String name;
  
  @Column(nullable = false, length = 500)
  private String url;
  
  @Column(length = 500)
  private String thumbnailUrl;
  
  @Column(columnDefinition = "TEXT")
  private String description;
  
  @Column(nullable = false)
  @Builder.Default
  private Integer priority = 0;
  
  @Column
  private LocalDateTime deletedAt;
  
  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;
  
  @Column(nullable = false)
  @UpdateTimestamp
  private LocalDateTime updatedAt;
  
  public boolean isDeleted() {
    return deletedAt != null;
  }
}