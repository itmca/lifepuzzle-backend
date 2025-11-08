package io.itmca.aivideogenerator.domain.video.entity;

import io.itmca.aivideogenerator.domain.video.type.VideoGenerationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Table(name = "ai_generated_video")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AiGeneratedVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long heroNo;

    @Column(nullable = false)
    private Long galleryId;

    @Column(nullable = false)
    private Long drivingVideoId;

    @Setter
    @Column(length = 500)
    private String videoUrl;

    @Setter
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private VideoGenerationStatus status = VideoGenerationStatus.PENDING;

    @Setter
    @Column
    private LocalDateTime startedAt;

    @Setter
    @Column
    private LocalDateTime completedAt;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String errorMessage;

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

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void markAsStarted() {
        this.status = VideoGenerationStatus.IN_PROGRESS;
        this.startedAt = LocalDateTime.now();
    }

    public void markAsCompleted(String videoUrl) {
        this.status = VideoGenerationStatus.COMPLETED;
        this.videoUrl = videoUrl;
        this.completedAt = LocalDateTime.now();
    }

    public void markAsFailed(String errorMessage) {
        this.status = VideoGenerationStatus.FAILED;
        this.errorMessage = errorMessage;
        this.completedAt = LocalDateTime.now();
    }
}