package io.itmca.lifepuzzle.domain.question.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Question {
  @Id
  @Column(name = "seq")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long questionNo;

  @Column
  String category;

  @Column(name = "question_content")
  String question;
}
