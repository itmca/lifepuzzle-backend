package io.itmca.lifepuzzle.domain.user.endpoint;

import io.itmca.lifepuzzle.domain.user.Mail;
import io.itmca.lifepuzzle.domain.user.entity.UserEmailValidation;
import io.itmca.lifepuzzle.domain.user.service.MailService;
import io.itmca.lifepuzzle.domain.user.service.UserEmailValidationService;
import io.itmca.lifepuzzle.domain.user.service.UserQueryService;
import io.itmca.lifepuzzle.global.exception.handler.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@Tag(name = "유저 검증")
public class UserValidateEndpoint {

  private final UserQueryService userQueryService;
  private final MailService mailService;
  private final UserEmailValidationService userEmailValidationService;

  @GetMapping("/dupcheck/email")
  @Operation(summary = "이메일 중복 체크")
  public boolean checkEmail(@RequestParam("email") String email) {
    try {
      userQueryService.findByEmail(email);
    } catch (NotFoundException e) {
      return false;
    }

    return true;
  }

  @GetMapping("/dupcheck/id")
  @Operation(summary = "아이디 중복 체크")
  public boolean checkId(@RequestParam("id") String id) {
    try {
      userQueryService.findByUserId(id);
    } catch (NotFoundException e) {
      return false;
    }

    return true;
  }

  @PostMapping("/email/verification")
  @Operation(summary = "이메일 검증 메일 발송")
  public HttpStatus sendVerificationEmail(@RequestParam("email") String email) {
    var code = new Random().nextInt(900000) + 100000;

    userEmailValidationService.create(
        UserEmailValidation.builder()
            .email(email)
            .code(String.valueOf(code))
            .build()
    );

    mailService.sendEmail(
        Mail.builder()
            .to(email)
            .from("***REMOVED***")
            .subject("[인생퍼즐] 이메일 인증 요청 메일입니다.")
            .html("6자리 인증 코드 : " + code)
            .build()
    );

    return HttpStatus.OK;
  }

  @PostMapping("/validation/email/code")
  @Operation(summary = "이메일 검증 코드 확인")
  public boolean checkUserByEmail(@RequestParam("email") String email,
                                  @RequestParam("code") String code) {
    UserEmailValidation userEmailValidation =
        userEmailValidationService.findRecentOneByEmail(email);

    return userEmailValidation.getCode().equals(code);
  }
}