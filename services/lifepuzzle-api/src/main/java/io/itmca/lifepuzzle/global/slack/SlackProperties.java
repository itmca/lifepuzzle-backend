package io.itmca.lifepuzzle.global.slack;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "slack")
@Data
public class SlackProperties {
  private String webhookUrl;
}
