package io.itmca.lifepuzzle.global.config;

import io.itmca.lifepuzzle.global.resolver.SentryExceptionResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
public class SentryConfig {
  @Bean
  public HandlerExceptionResolver resolveSentryException() {
    return new SentryExceptionResolver();
  }
}
