package io.itmca.lifepuzzle.domain.sites;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SharePageController {

  @GetMapping("/share/hero")
  public String shareHeroPage() {
    return "share-app-landing";
  }
}
