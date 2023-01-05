package io.itmca.lifepuzzle.domain;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthEndpoint {
    @RequestMapping({"", "hc"})
    public String healthCheck(){
        return "ok";
    }
}
