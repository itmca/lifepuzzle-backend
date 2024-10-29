package io.itmca.lifepuzzle.domain.story.entity;

import io.itmca.lifepuzzle.domain.story.LikeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "likes")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Like {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long userId;
  @Enumerated(EnumType.STRING)
  private LikeType type;
  private String targetId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "targetId", referencedColumnName = "id", insertable = false, updatable = false)
  private Story story;

  @CreationTimestamp
  private LocalDateTime createdAt;
}
