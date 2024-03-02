package io.itmca.lifepuzzle.domain.hero.entity;

import io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus;
import io.itmca.lifepuzzle.domain.user.entity.User;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "user_hero_auth")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HeroUserAuth {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long seq;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userNo")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "heroNo")
  private Hero hero;
  @Enumerated(EnumType.STRING)
  private HeroAuthStatus auth;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(nullable = false)
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  public void changeAuth(HeroAuthStatus heroAuthStatus) {
    auth = heroAuthStatus;
  }

}
