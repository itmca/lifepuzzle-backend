package io.itmca.lifepuzzle.domain.user.endpoint;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserValidateEndpoint {

    @GetMapping("dupcheck/email/{email}")
    public void checkEmail(@PathVariable("email") String email) {}

    @GetMapping("dupcheck/id/{id}")
    public void checkId(@PathVariable String id) {}

    @PostMapping("email/verification")
    public void sendVerificationEmail(@RequestBody String email) {}

    @PostMapping("validation/email/code")
    public void checkUserByEmail(@RequestBody String email, @RequestBody String code) {}
}
