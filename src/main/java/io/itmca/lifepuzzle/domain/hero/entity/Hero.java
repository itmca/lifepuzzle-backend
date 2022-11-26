package io.itmca.lifepuzzle.domain.hero.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
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

//    @OneToMany(mappedBy = "hero")
//    private List<HeroUserAuth> heroUserAuths = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime updatedAt;
//
//    public boolean canBeAccessedBy(Long userNo){
//        return false;
//    }
//
//    public void addAccessibleUser(HeroUserAuth heroUserAuth){
//        heroUserAuth.setHero(this);
//
//        this.heroUserAuths.add(heroUserAuth);
//    }
}
