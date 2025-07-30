package io.itmca.lifepuzzle.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI openAPI() {
    Info info = new Info()
        .title("인생퍼즐 API")
        .version("v0.1")
        .description("인생퍼즐 API 명세");

    return new OpenAPI()
        .components(new Components())
        .info(info)
        .components(new io.swagger.v3.oas.models.Components()
            .addSecuritySchemes("Bearer Authentication", new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")))
        // 보안 요구 사항 정의 (Swagger UI에서 Bearer Authentication 사용을 명시)
        .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"));
  }
}
