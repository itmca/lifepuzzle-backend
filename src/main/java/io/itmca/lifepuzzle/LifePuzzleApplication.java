package io.itmca.lifepuzzle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class LifePuzzleApplication {

	public static void main(String[] args) {
		SpringApplication.run(LifePuzzleApplication.class, args);
	}

}
