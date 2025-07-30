package io.itmca.lifepuzzle.domain.sites;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * iOS Universal Link에 필요한 json을 응답하기 위한 Endpoint.
 * 해당 처리만을 위한 별도 서버를 둘 수 없어 해당 서버에서 처리하도록 함
 */
@RestController
public class UniversalLinkEndpoint {

  @GetMapping("/.well-known/apple-app-site-association")
  public String appleAppSiteAssociation() {
    return """
            {
               "applinks": {
                   "details": [
                        {
                          "appIDs": [ "***REMOVED***.io.itmca.lifepuzzle" ],
                          "components": [{ "/": "/share/hero"}]
                        }
                    ]
                }
             }
        """;
  }
}
