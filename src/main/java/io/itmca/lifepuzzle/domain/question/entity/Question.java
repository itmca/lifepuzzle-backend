package io.itmca.lifepuzzle.domain.question.entity;

import lombok.*;

import javax.persistence.*;

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
