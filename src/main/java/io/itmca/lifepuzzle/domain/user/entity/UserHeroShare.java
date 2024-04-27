package io.itmca.lifepuzzle.domain.user.entity;

import io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
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
  long ownerNo;

  @Column(nullable = false)
  long heroNo;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  HeroAuthStatus auth;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false, updatable = false)
  private LocalDateTime expiredAt;
}
