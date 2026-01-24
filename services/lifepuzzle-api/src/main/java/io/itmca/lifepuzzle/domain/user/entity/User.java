package io.itmca.lifepuzzle.domain.user.entity;

import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.user.endpoint.request.UserUpdateRequest;
import io.itmca.lifepuzzle.domain.user.type.UserType;
import io.itmca.lifepuzzle.global.file.domain.UserProfileImage;
import io.itmca.lifepuzzle.global.util.PasswordUtil;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.StringUtils;

@Entity
@Table(name = "user")
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String loginId;
  private String email;
  private String salt;
  private String password;
  private LocalDate birthday;
  private Long recentHero;
  private boolean emailValidated;
  private String nickName;
  private String kakaoId;
  private String appleId;
  private boolean pushOptIn;
  private String image;

  @OneToMany(mappedBy = "user")
  private List<HeroUserAuth> heroUserAuths;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

  public String getUserType() {
    if (StringUtils.hasText(this.appleId)) {
      return UserType.APPLE.frontEndKey();
    } else if (StringUtils.hasText(this.kakaoId)) {
      return UserType.KAKAO.frontEndKey();
    }
    return UserType.GENERAL.frontEndKey();
  }

  public void changeRecentHero(Long heroNo) {
    this.recentHero = heroNo;
  }

  public void changePassword(String password) {
    this.password = password;
  }

  public void setNickname(String nickname) {
    this.nickName = nickname;
  }

  public void hashCredential(String password) {
    var newSalt = PasswordUtil.genSalt();
    var hashedPassword = PasswordUtil.hashPassword(password, newSalt);

    this.salt = newSalt;
    this.password = hashedPassword;
  }

  public void updateUserInfo(UserUpdateRequest userUpdateRequest) {
    setNickname(userUpdateRequest.nickName());
  }

  public void setProfileImage(UserProfileImage userProfileImage) {
    if (userProfileImage == null) {
      this.image = null;
    } else {
      this.image = userProfileImage.getFileName();
    }
  }
}
