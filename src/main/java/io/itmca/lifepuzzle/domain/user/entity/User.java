package io.itmca.lifepuzzle.domain.user.entity;

<<<<<<< Updated upstream
=======
import io.itmca.lifepuzzle.domain.user.LoginType;
import lombok.*;
import org.springframework.util.StringUtils;

>>>>>>> Stashed changes
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    private Long userNo;
<<<<<<< Updated upstream
=======
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
>>>>>>> Stashed changes
}
