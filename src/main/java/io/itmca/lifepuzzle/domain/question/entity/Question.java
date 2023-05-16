package io.itmca.lifepuzzle.domain.question.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
