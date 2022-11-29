package io.itmca.lifepuzzle.domain.user.endpoint;

import io.itmca.lifepuzzle.domain.user.entity.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "user")
public class UserHeroEndpoint {

    @GetMapping("/")
    public String test() {
        return "Hello World";
    }

    @PostMapping("/hero/recent")
    public void updateRecentHero(@RequestBody String heroNo) {}
}