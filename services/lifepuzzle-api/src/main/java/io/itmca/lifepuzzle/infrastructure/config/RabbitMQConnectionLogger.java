package io.itmca.lifepuzzle.infrastructure.config;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.stream.binder.rabbit.RabbitMessageChannelBinder;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitMQConnectionLogger {

  @Autowired
  private Environment environment;

  @Autowired(required = false)
  private CachingConnectionFactory connectionFactory;

  @Autowired(required = false)
  private RabbitMessageChannelBinder rabbitMessageChannelBinder;

  @EventListener(ApplicationReadyEvent.class)
  public void logRabbitMQConnectionInfo() {
    log.info("=== RabbitMQ Connection Information ===");
    
    // Environment variables and properties
    logProperty("RABBITMQ_HOST", environment.getProperty("spring.rabbitmq.host"));
    logProperty("RABBITMQ_PORT", environment.getProperty("spring.rabbitmq.port"));
    logProperty("RABBITMQ_USERNAME", environment.getProperty("spring.rabbitmq.username"));
    logProperty("RABBITMQ_PASSWORD", maskPassword(environment.getProperty("spring.rabbitmq.password")));
    logProperty("RABBITMQ_ADMIN_PORT", environment.getProperty("RABBITMQ_ADMIN_PORT"));
    
    // Spring Cloud Stream specific
    logProperty("Admin Addresses", environment.getProperty("spring.cloud.stream.rabbit.binder.admin-addresses"));
    logProperty("Connection Name Prefix", environment.getProperty("spring.cloud.stream.rabbit.binder.connection-name-prefix"));
    
    // Active profile
    String[] activeProfiles = environment.getActiveProfiles();
    log.info("Active Profiles: {}", String.join(", ", activeProfiles));
    
    // Connection factory details
    if (connectionFactory != null) {
      log.info("Connection Factory Details:");
      log.info("  - Host: {}", connectionFactory.getHost());
      log.info("  - Port: {}", connectionFactory.getPort());
      log.info("  - Username: {}", connectionFactory.getUsername());
      log.info("  - Virtual Host: {}", connectionFactory.getVirtualHost());
      try {
        log.info("  - Connection Name: {}", connectionFactory.toString().contains("connectionNamePrefix") 
            ? "Set via configuration" : "Default");
      } catch (Exception e) {
        log.info("  - Connection Name: Unable to determine");
      }
    } else {
      log.warn("CachingConnectionFactory not available");
    }
    
    // Additional system properties that might affect connection
    logSystemProperty("RABBITMQ_HOST");
    logSystemProperty("RABBITMQ_PORT");
    logSystemProperty("RABBITMQ_USERNAME");
    logSystemProperty("RABBITMQ_PASSWORD");
    logSystemProperty("RABBITMQ_ADMIN_PORT");
    
    log.info("======================================");
  }
  
  private void logProperty(String name, String value) {
    if (value != null) {
      log.info("{}: {}", name, value);
    } else {
      log.info("{}: [NOT SET]", name);
    }
  }
  
  private void logSystemProperty(String propertyName) {
    String value = System.getProperty(propertyName);
    if (value == null) {
      value = System.getenv(propertyName);
    }
    
    if (value != null) {
      if (propertyName.toLowerCase().contains("password")) {
        value = maskPassword(value);
      }
      log.info("System/Environment {}: {}", propertyName, value);
    }
  }
  
  private String maskPassword(String password) {
    if (password == null || password.isEmpty()) {
      return "[EMPTY]";
    }
    if (password.length() <= 2) {
      return "***";
    }
    return password.substring(0, 1) + "***" + password.substring(password.length() - 1);
  }
}