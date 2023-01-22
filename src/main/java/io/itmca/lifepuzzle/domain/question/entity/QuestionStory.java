package io.itmca.lifepuzzle.domain.question.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    Boolean isQuestionModified;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    LocalDateTime createdAt;
}
