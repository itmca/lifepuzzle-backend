package io.itmca.lifepuzzle.domain.sites;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Android App Link에 필요한 json을 응답하기 위한 Controller.
 * 해당 처리만을 위한 별도 서버를 둘 수 없어 해당 서버에서 처리하도록 함
 */
@RestController
public class AndroidAppLinkController {

  @GetMapping("/.well-know/assetlinks.json")
  public String applinkAsset() {
    return """
            [{
              "relation": ["delegate_permission/common.handle_all_urls"],
              "target": {
                "namespace": "android_app",
                "package_name": "io.itmca.lifepuzzle",
                "sha256_cert_fingerprints": [" FA:C6:17:45:DC:09:03:78:6F:B9:ED:E6:2A:96:2B:39:9F:73:48:F0:BB:6F:89:9B:83:32:66:75:91:03:3B:9C"]
              }
            }]
        """;
  }
}
