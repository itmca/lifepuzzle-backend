package io.itmca.lifepuzzle.domain.user.endpoint;

import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import io.itmca.lifepuzzle.domain.user.endpoint.request.UserPasswordUpdateRequest;
import io.itmca.lifepuzzle.domain.user.endpoint.request.UserRegisterRequest;
import io.itmca.lifepuzzle.domain.user.endpoint.request.UserUpdateRequest;
import io.itmca.lifepuzzle.domain.user.endpoint.request.UserWithdrawRequest;
import io.itmca.lifepuzzle.domain.user.endpoint.response.dto.UserQueryDto;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.domain.user.model.PasswordVerification;
import io.itmca.lifepuzzle.domain.user.service.RegisterService;
import io.itmca.lifepuzzle.domain.user.service.UserWriteService;
import io.itmca.lifepuzzle.domain.user.service.WithdrawService;
import io.itmca.lifepuzzle.global.exception.PasswordMismatchException;
import io.itmca.lifepuzzle.global.exception.UserNoMismatchException;
import io.itmca.lifepuzzle.global.resolver.CurrentUser;
import io.itmca.lifepuzzle.global.util.PasswordUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "유저 수정")
public class UserWriteEndpoint {
  private final RegisterService registerService;
  private final WithdrawService withdrawService;
  private final UserWriteService userWriteService;

  @RequestMapping(value = {"/v1/users/{id}"}, method = {PUT, PATCH})
  @Operation(summary = "유저 수정")
  public UserQueryDto updateUser(@PathVariable("id") Long id,
                                 @RequestPart("toUpdate") UserUpdateRequest userUpdateRequest,
                                 @RequestPart(required = false)
                                 MultipartFile photo,
                                 @CurrentUser User user) {
    if (!id.equals(user.getId())) {
      throw new UserNoMismatchException();
    }

    return UserQueryDto.from(userWriteService.update(user, userUpdateRequest, photo));
  }

  @RequestMapping(value = {"/v1/users/{id}/password"},
      method = {PUT, PATCH})
  @Operation(summary = "비밀번호 변경")
  public void updateUserPassword(@PathVariable("id") Long id,
                                 @CurrentUser User user,
                                 @RequestBody UserPasswordUpdateRequest userPasswordUpdateRequest) {
    if (!id.equals(user.getId())) {
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

  @Operation(summary = "회원가입")
  @PostMapping({"/v1/users"})
  public HttpStatus register(@RequestBody UserRegisterRequest userRegisterRequest) {
    var user = userRegisterRequest.toUser();

    registerService.register(user, userRegisterRequest.getShareKey());

    return HttpStatus.OK;
  }

  @DeleteMapping({"/v1/users/{id}"})
  @Operation(summary = "회원탈퇴")
  public void withdraw(@PathVariable("id") Long id, @CurrentUser User user,
                       @RequestBody(required = false) UserWithdrawRequest userWithdrawRequest) {
    if (!id.equals(user.getId())) {
      throw new UserNoMismatchException();
    }

    withdrawService.withdraw(user, userWithdrawRequest);
  }

}
