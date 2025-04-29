package io.itmca.lifepuzzle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class LifePuzzleApplication {

  static {
    System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
  }

  public static void main(String[] args) {
    SpringApplication.run(LifePuzzleApplication.class, args);
  }

}
