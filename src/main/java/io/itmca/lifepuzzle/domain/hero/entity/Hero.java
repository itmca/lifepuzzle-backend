package io.itmca.lifepuzzle.domain.hero.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Hero {

    @Id
    @Column(name = "seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long heroNo;
    private Long parentNo;
    private Long spouseNo;
    private String name;
    private String nickname;
    private LocalDateTime birthday;
    private String image;
    private String title;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime updatedAt;

}
