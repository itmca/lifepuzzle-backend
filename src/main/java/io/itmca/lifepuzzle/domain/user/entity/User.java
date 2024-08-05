package io.itmca.lifepuzzle.domain.user.entity;

import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.user.UserType;
import io.itmca.lifepuzzle.domain.user.endpoint.request.UserUpdateRequest;
import io.itmca.lifepuzzle.domain.user.file.UserProfileImage;
import io.itmca.lifepuzzle.global.util.PasswordUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id
  @Column(name = "seq")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userNo;
  @Column(name = "id")
  private String userId;
  private String email;
  private String salt;
  private String password;
  private LocalDate birthday;
  @Column(name = "recent_hero")
  private Long recentHeroNo;
  private Boolean validated;
  @Column(name = "nick_name")
  private String nickName;
  @Column(name = "kakao_id")
  private String kakaoId;
  @Column(name = "apple_id")
  private String appleId;
  @Column(name = "email_notice")
  private Boolean emailNotice;
  @Column(name = "phone_notice")
  private Boolean phoneNotice;
  @Column(name = "kakao_notice")
  private Boolean kakaoNotice;
  @Column(name = "inapp_notice")
  private Boolean inappNotice;
  private String image;

  @OneToMany(mappedBy = "user")
  private List<HeroUserAuth> heroUserAuths;

  @Column(name = "created_at")
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
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

  public void changeRecentHeroNo(Long heroNo) {
    this.recentHeroNo = heroNo;
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
    this.email = userUpdateRequest.getEmail();
    this.birthday = userUpdateRequest.getBirthday();
    this.nickName = userUpdateRequest.getNickName();
  }

  public void setImage(UserProfileImage userProfileImage) {
    this.image = userProfileImage.getFileName();
  }
}
