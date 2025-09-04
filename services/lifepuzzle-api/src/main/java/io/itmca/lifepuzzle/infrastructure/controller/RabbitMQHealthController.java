package io.itmca.lifepuzzle.infrastructure.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/health")
public class RabbitMQHealthController {

  @Autowired
  private Environment environment;

  @Autowired(required = false)
  private CachingConnectionFactory connectionFactory;

  @GetMapping("/rabbitmq")
  public ResponseEntity<Map<String, Object>> checkRabbitMQHealth() {
    Map<String, Object> healthInfo = new HashMap<>();
    
    try {
      // Configuration details
      healthInfo.put("timestamp", LocalDateTime.now());
      healthInfo.put("configuration", getRabbitMQConfiguration());
      
      // Connection status
      if (connectionFactory != null) {
        healthInfo.put("connectionFactory", getConnectionFactoryInfo());
        
        // Try to get a connection to test connectivity
        try {
          var connection = connectionFactory.createConnection();
          healthInfo.put("status", "UP");
          healthInfo.put("connected", true);
          healthInfo.put("connectionDetails", Map.of(
              "connectionInfo", connection.toString(),
              "isOpen", connection.isOpen()
          ));
          if (connection.isOpen()) {
            connection.close();
          }
        } catch (Exception e) {
          log.error("Failed to connect to RabbitMQ", e);
          healthInfo.put("status", "DOWN");
          healthInfo.put("connected", false);
          healthInfo.put("error", e.getMessage());
        }
      } else {
        healthInfo.put("status", "DOWN");
        healthInfo.put("connected", false);
        healthInfo.put("error", "ConnectionFactory not available");
      }
      
      return ResponseEntity.ok(healthInfo);
      
    } catch (Exception e) {
      log.error("Error checking RabbitMQ health", e);
      healthInfo.put("status", "ERROR");
      healthInfo.put("error", e.getMessage());
      healthInfo.put("timestamp", LocalDateTime.now());
      return ResponseEntity.internalServerError().body(healthInfo);
    }
  }
  
  private Map<String, Object> getRabbitMQConfiguration() {
    Map<String, Object> config = new HashMap<>();
    
    config.put("host", environment.getProperty("spring.rabbitmq.host"));
    config.put("port", environment.getProperty("spring.rabbitmq.port"));
    config.put("username", environment.getProperty("spring.rabbitmq.username"));
    config.put("password", maskPassword(environment.getProperty("spring.rabbitmq.password")));
    config.put("virtualHost", environment.getProperty("spring.rabbitmq.virtual-host", "/"));
    config.put("adminAddresses", environment.getProperty("spring.cloud.stream.rabbit.binder.admin-addresses"));
    config.put("connectionNamePrefix", environment.getProperty("spring.cloud.stream.rabbit.binder.connection-name-prefix"));
    config.put("activeProfiles", String.join(",", environment.getActiveProfiles()));
    
    return config;
  }
  
  private Map<String, Object> getConnectionFactoryInfo() {
    Map<String, Object> info = new HashMap<>();
    
    if (connectionFactory != null) {
      info.put("host", connectionFactory.getHost());
      info.put("port", connectionFactory.getPort());
      info.put("username", connectionFactory.getUsername());
      info.put("virtualHost", connectionFactory.getVirtualHost());
      info.put("channelCacheSize", connectionFactory.getChannelCacheSize());
      info.put("connectionCacheSize", connectionFactory.getConnectionCacheSize());
    }
    
    return info;
  }
  
  private String maskPassword(String password) {
    if (password == null || password.isEmpty()) {
      return "[NOT SET]";
    }
    if (password.length() <= 2) {
      return "***";
    }
    return password.substring(0, 1) + "***" + password.substring(password.length() - 1);
  }
}