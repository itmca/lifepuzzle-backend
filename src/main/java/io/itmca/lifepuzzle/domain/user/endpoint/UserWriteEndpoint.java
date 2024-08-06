package io.itmca.lifepuzzle.domain.user.endpoint;

import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import io.itmca.lifepuzzle.domain.register.PasswordVerification;
import io.itmca.lifepuzzle.domain.user.CurrentUser;
import io.itmca.lifepuzzle.domain.user.endpoint.request.UserPasswordUpdateRequest;
import io.itmca.lifepuzzle.domain.user.endpoint.request.UserUpdateRequest;
import io.itmca.lifepuzzle.domain.user.endpoint.response.UserQueryDto;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.domain.user.file.UserProfileImage;
import io.itmca.lifepuzzle.domain.user.service.UserWriteService;
import io.itmca.lifepuzzle.global.exception.PasswordMismatchException;
import io.itmca.lifepuzzle.global.exception.UserNoMismatchException;
import io.itmca.lifepuzzle.global.infra.file.service.S3UploadService;
import io.itmca.lifepuzzle.global.util.PasswordUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
@Tag(name = "유저 수정")
public class UserWriteEndpoint {

  private final UserWriteService userWriteService;
  private final S3UploadService s3UploadService;

  @RequestMapping(value = "/{id}", method = {PUT, PATCH})
  @Operation(summary = "유저 수정")
  public UserQueryDto updateUser(@PathVariable("id") Long id,
                                 @RequestPart("toUpdate") UserUpdateRequest userUpdateRequest,
                                 @RequestPart(name = "photo", required = false)
                                 MultipartFile requestPhoto,
                                 @CurrentUser User user) {
    if (id != user.getUserNo()) {
      throw new UserNoMismatchException();
    }

    if (requestPhoto != null) {
      var userProfileImage = new UserProfileImage(user, requestPhoto);

      user.setImage(userProfileImage);

      s3UploadService.upload(userProfileImage);
    }

    user.updateUserInfo(userUpdateRequest);

    return UserQueryDto.from(userWriteService.save(user));
  }

  @RequestMapping(value = "/{id}/password", method = {PUT, PATCH})
  @Operation(summary = "비밀번호 변경")
  public void updateUserPassword(@PathVariable("id") Long id,
                                 @CurrentUser User user,
                                 @RequestBody UserPasswordUpdateRequest userPasswordUpdateRequest) {
    if (id != user.getUserNo()) {
      throw new UserNoMismatchException();
    }

    var isMatch = PasswordUtil.matches(
        PasswordVerification.builder()
            .plainPassword(userPasswordUpdateRequest.getOldPassword())
            .salt(user.getSalt())
            .hashedPassword(user.getPassword())
            .build()
    );

    if (!isMatch) {
      throw new PasswordMismatchException();
    }

    userWriteService.updateUserPassword(user, userPasswordUpdateRequest.getNewPassword());
  }

}
