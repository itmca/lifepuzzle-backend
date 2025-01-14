package io.itmca.lifepuzzle.global.config;

import io.itmca.lifepuzzle.domain.auth.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .httpBasic(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            (request) -> request.requestMatchers(
                    "/", "/hc", "/error-test", "/auth/**",
                    "/user", "/user/dupcheck/*", "/users", "/users/dupcheck/*",
                    "/v3/**", "/question/*", "/questions/*",
                    "/share/hero", "/swagger-ui/**", "/stories/**",
                    "/.well-know/assetlinks.json", "/.well-known/assetlinks.json",
                    ".well-known/apple-app-site-association")
                .permitAll()
                .anyRequest()
                .authenticated()
        )
        .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
