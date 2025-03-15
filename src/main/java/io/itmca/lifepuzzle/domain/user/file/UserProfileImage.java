package io.itmca.lifepuzzle.domain.user.file;

import static io.itmca.lifepuzzle.global.constants.FileConstant.USER_PROFILE_IMAGE_BASE_PATH_FORMAT;

import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.global.file.CustomFile;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class UserProfileImage extends CustomFile {
  public UserProfileImage(User user, MultipartFile file, String postfix) {
    super(
        USER_PROFILE_IMAGE_BASE_PATH_FORMAT.formatted(user.getId().toString()),
        file,
        postfix
    );
  }
}
