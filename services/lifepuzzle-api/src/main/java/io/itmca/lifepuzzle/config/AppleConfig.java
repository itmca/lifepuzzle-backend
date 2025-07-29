package io.itmca.lifepuzzle.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "apple")
@Getter
@RequiredArgsConstructor
public class AppleConfig {
  
  private final String teamId;
  private final String bundleId;
  private final String privateKeyId;
  private final String privateKey;
}