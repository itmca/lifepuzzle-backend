package io.itmca.lifepuzzle.domain.user.endpoint;

import io.itmca.lifepuzzle.domain.user.Mail;
import io.itmca.lifepuzzle.domain.user.entity.UserEmailValidation;
import io.itmca.lifepuzzle.domain.user.service.MailService;
import io.itmca.lifepuzzle.domain.user.service.UserEmailValidationService;
import io.itmca.lifepuzzle.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserValidateEndpoint {

    private final UserQueryService userQueryService;
    private final MailService mailService;
    private final UserEmailValidationService userEmailValidationService;

    @GetMapping("/dupcheck/email")
    public boolean checkEmail(@RequestParam("email") String email) {
        var isDuplicated = false;
        var user = userQueryService.findByEmail(email);
        if (user != null) isDuplicated = true;

        return isDuplicated;
    }

    @GetMapping("/dupcheck/id")
    public boolean checkId(@RequestParam("id") String id) {
        var isDuplicated = false;
        var user = userQueryService.findByUserId(id);
        if (user != null) isDuplicated = true;

        return isDuplicated;
    }

    @PostMapping("/email/verification")
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
    public boolean checkUserByEmail(@RequestParam("email") String email, @RequestParam("code") String code) {
        UserEmailValidation userEmailValidation = userEmailValidationService.findRecentOneByEmail(email);
        return userEmailValidation.getCode().equals(code);
    }
}