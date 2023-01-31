package io.itmca.lifepuzzle.domain.hero.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
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
    private LocalDate birthday;
    private String image;
    private String title;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public static Hero defaultHero() {
        return Hero.builder()
                .name("주인공")
                .nickname("소중한 분")
                .title("봄날의 햇살처럼 따뜻한 당신")
                .birthday(LocalDate.of(1970, 1, 1))
                .image("")
                .build();
    }

}
