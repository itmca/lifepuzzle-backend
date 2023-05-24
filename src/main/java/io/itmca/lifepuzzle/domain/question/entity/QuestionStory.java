package io.itmca.lifepuzzle.domain.question.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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
  String storyKey;

  @Column
  Long heroNo;

  @Column
  Long recQuestionNo;

  @Column
  boolean isQuestionModified;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  LocalDateTime createdAt;
}
