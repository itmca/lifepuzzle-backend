package io.itmca.lifepuzzle.domain.sites;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 주인공 공유 페이지를 위한 Controller.
 * 해당 페이지만을 위한 별도 서버를 둘 수 없어 해당 서버에서 처리하도록 함
 */
@Controller
public class SharePageController {

  @GetMapping("/share/hero")
  public String shareHeroPage() {
    return "share-app-landing";
  }
}
