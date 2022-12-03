package io.itmca.lifepuzzle.domain.user.entity;

import io.itmca.lifepuzzle.domain.user.LoginType;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private Long userNo;
    private String userId;
    private String email;
    private String salt;
    private String password;
    private LocalDate birthday;
    private Long recentHeroNo;
    private boolean validated;
    private String nickName;
    private String kakaoId;
    private String appleId;
    private boolean emailNotice;
    private boolean phoneNotice;
    private boolean kakaoNotice;
    private boolean inappNotice;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public LoginType getUserType() {
        if (StringUtils.hasText(this.appleId))
            return LoginType.APPLE;
        else if (StringUtils.hasText(this.kakaoId))
            return LoginType.KAKAO;
        return LoginType.GENERAL;
    }
}
