package io.itmca.lifepuzzle.domain.user.endpoint;

import org.springframework.web.bind.annotation.*;

@RestController
public class UserValidateEndpoint {

    @GetMapping("/user/dupcheck/email")
    public void checkEmail(@RequestParam("email") String email) {
    }

    @GetMapping("/user/dupcheck/id")
    public String checkId(@RequestParam("id") String id) {
        return null;
    }

    @PostMapping("/user/email/verification")
    public void sendVerificationEmail(@RequestParam String email) {
    }

    @PostMapping("/user/validation/email/code")
    public void checkUserByEmail(@RequestBody String email, @RequestBody String code) {
    }
}
