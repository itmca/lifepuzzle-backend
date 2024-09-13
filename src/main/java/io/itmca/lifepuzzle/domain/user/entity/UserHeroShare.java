package io.itmca.lifepuzzle.domain.user.entity;

import io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserHeroShare {
  @Id
  @Column(nullable = false)
  String id;

  @Column(nullable = false)
  long sharerUserId;

  @Column(nullable = false)
  long heroId;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  HeroAuthStatus auth;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false, updatable = false)
  private LocalDateTime expiredAt;
}
