package io.itmca.lifepuzzle.domain.user.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
