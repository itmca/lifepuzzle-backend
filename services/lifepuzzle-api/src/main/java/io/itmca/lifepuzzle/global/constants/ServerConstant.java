package io.itmca.lifepuzzle.global.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerConstant {
  
  public static String S3_SERVER_HOST;
  
  public static final String DEEP_LINK_SERVER_HOST =
      "https://lifepuzzle.itmca.io/";
      
  @Value("${app.s3.server-host:}")
  public void setS3ServerHost(String s3ServerHost) {
    S3_SERVER_HOST = s3ServerHost;
  }
}
