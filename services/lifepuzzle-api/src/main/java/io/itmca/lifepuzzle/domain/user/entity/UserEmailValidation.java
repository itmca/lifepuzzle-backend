package io.itmca.lifepuzzle.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity(name = "user_email_validation")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEmailValidation {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long seq;
  private String email;
  private String code;
  @Column(name = "created_at")
  @CreationTimestamp
  private LocalDate createdAt;
}
