package io.itmca.lifepuzzle.domain.user.endpoint;

import io.itmca.lifepuzzle.domain.register.PasswordVerification;
import io.itmca.lifepuzzle.domain.user.CurrentUser;
import io.itmca.lifepuzzle.domain.user.endpoint.request.UserPasswordUpdateRequest;
import io.itmca.lifepuzzle.domain.user.endpoint.request.UserUpdateRequest;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.domain.user.service.UserWriteService;
import io.itmca.lifepuzzle.global.util.PasswordUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
@Tag(name = "유저 정보 수정")
public class UserWriteEndpoint {

  private final UserWriteService userWriteService;

  @PatchMapping("/{id}")
  @Operation(summary = "유저 정보 수정")
  public void updateUser(@PathVariable("id") Long id,
                         @Parameter(hidden = true) @CurrentUser User user,
                         @RequestBody UserUpdateRequest userUpdateRequest) {
//    if (id != user.getUserNo()) {
//      // exception
//    }
//
//    user.updateUserInfo(userUpdateRequest);
//
//    userWriteService.save(user);
  }

  @PatchMapping("/{id}/password")
  @Operation(summary = "비밀번호 변경")
  public void updateUserPassword(@PathVariable("id") Long id,
                                 @Parameter(hidden = true) @CurrentUser User user,
                                 @RequestBody UserPasswordUpdateRequest userPasswordUpdateRequest) {
    if (id != user.getUserNo()) {
      // exception
    }

    var isMatch = PasswordUtil.matches(
        PasswordVerification.builder()
            .plainPassword(userPasswordUpdateRequest.getOldPassword())
            .salt(user.getSalt())
            .hashedPassword(user.getPassword())
            .build()
    );

    if (!isMatch) {
      // exception
    }

    userWriteService.updateUserPassword(user, userPasswordUpdateRequest.getNewPassword());
  }

}
