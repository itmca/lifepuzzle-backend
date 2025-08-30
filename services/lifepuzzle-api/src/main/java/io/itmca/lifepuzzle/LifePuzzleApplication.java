package io.itmca.lifepuzzle;

import io.itmca.lifepuzzle.config.AppleConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@EnableConfigurationProperties(AppleConfig.class)
public class LifePuzzleApplication {

  static {
    System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
  }

  // TODO: 앱 정상 배포 확인을 위한 로그로 확인 후 제거 필요
  public static void main(String[] args) {
    System.out.println("Starting LifePuzzleApplication");
    SpringApplication.run(LifePuzzleApplication.class, args);
    System.out.println("Started LifePuzzleApplication - 20250829");
  }

}
