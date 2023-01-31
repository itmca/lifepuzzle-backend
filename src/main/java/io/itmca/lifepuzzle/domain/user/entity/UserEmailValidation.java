package io.itmca.lifepuzzle.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

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
