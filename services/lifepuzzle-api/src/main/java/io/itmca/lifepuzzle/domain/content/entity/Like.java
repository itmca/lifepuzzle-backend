package io.itmca.lifepuzzle.domain.content.entity;

import io.itmca.lifepuzzle.domain.content.type.LikeType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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
