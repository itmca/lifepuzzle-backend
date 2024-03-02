package io.itmca.lifepuzzle.domain.hero.entity;

import io.itmca.lifepuzzle.domain.hero.file.HeroProfileImage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Hero {

  @Id
  @Column(name = "seq")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long heroNo;
  private Long parentNo;
  private Long spouseNo;
  private String name;
  private String nickname;
  private LocalDate birthday;
  private String image;
  private String title;

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

  public void setImage(HeroProfileImage heroProfileImage) {
    this.image = heroProfileImage.getFileName();
  }

}
