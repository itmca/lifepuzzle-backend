package io.itmca.lifepuzzle.domain.hero.endpoint;

import io.itmca.lifepuzzle.domain.hero.endpoint.response.HeroQueryDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("heroes")
public class HeroQueryEndpoint {

    @GetMapping()
    public HeroQueryDTO getHeroes(@RequestParam String user) {};

    @GetMapping("/{heroNo}")
    public HeroQueryDTO getHeroDetail(@PathVariable int heroNo){};

}
