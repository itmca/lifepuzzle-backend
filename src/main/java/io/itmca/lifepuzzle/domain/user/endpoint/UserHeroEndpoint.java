package io.itmca.lifepuzzle.domain.user.endpoint;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "user")
public class UserHeroEndpoint {

    @GetMapping("/test")
    public String test() {
        return "Hello World";
    }

    @PostMapping("/hero/recent")
    public String updateRecentHero(@RequestBody String heroNo) {
        return heroNo;
    }
}
