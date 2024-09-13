package io.itmca.lifepuzzle.domain.question.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Table(name = "story")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionStory {
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  String id;

  @Column
  Long heroId;

  @Column
  Long recQuestionId;

  @Column
  boolean isQuestionModified;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  LocalDateTime createdAt;
}
