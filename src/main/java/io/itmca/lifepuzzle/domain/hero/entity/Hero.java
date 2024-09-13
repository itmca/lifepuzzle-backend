package io.itmca.lifepuzzle.domain.hero.entity;

import io.itmca.lifepuzzle.domain.hero.file.HeroProfileImage;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.Nullable;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Hero {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long heroNo;
  @Nullable
  private Long parentId;
  @Nullable
  private Long spouseId;
  @Setter
  private String name;
  @Setter
  private String nickname;
  @Setter
  private LocalDate birthday;
  @Setter
  private String title;
  private String image;

  @OneToMany(mappedBy = "hero")
  private List<HeroUserAuth> heroUserAuths;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(nullable = false)
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  public static Hero defaultHero() {
    return Hero.builder()
        .name("주인공")
        .nickname("소중한 분")
        .title("봄날의 햇살처럼 따뜻한 당신")
        .birthday(LocalDate.of(1970, 1, 1))
        .image("")
        .build();
  }

  public void setProfileImage(@Nullable HeroProfileImage heroProfileImage) {
    if (heroProfileImage == null) {
      return;
    }

    this.image = heroProfileImage.getFileName();
  }
}
