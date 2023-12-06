package io.itmca.lifepuzzle.domain.user.file;

import static io.itmca.lifepuzzle.global.constant.FileConstant.USER_PROFILE_BASE_PATH;

import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.global.infra.file.CustomFile;
import java.io.File;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class UserProfileImage extends CustomFile {
  public UserProfileImage(User user, MultipartFile file) {
    super(
        String.join(File.separator, USER_PROFILE_BASE_PATH, user.getUserNo().toString(), "image"),
        file);
  }
}
