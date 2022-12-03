package io.itmca.lifepuzzle.domain.user.endpoint;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserValidateEndpoint {

    @GetMapping("dupcheck/email")
    public void checkEmail(@RequestParam("email") String email) {
    }

    @GetMapping("dupcheck/id")
    public String checkId(@RequestParam("id") String id) {
    }

    @PostMapping("email/verification")
    public void sendVerificationEmail(@Request String email) {
    }

    @PostMapping("validation/email/code")
    public void checkUserByEmail(@RequestBody String email, @RequestBody String code) {
    }
}
