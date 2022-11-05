package io.itmca.lifepuzzle.domain.hero.endpoint;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller("heroes")
public class HeroWriteEndpoint {

    @PostMapping("")
    public void createHero() {}

    @PutMapping("/{heroNo}")
    public void updateHero() {}

    @DeleteMapping("/{heroNo}")
    public void deleteHero() {}

    @PostMapping("/profile/{heroNo}")
    public void saveHeroPhoto() {}
}
