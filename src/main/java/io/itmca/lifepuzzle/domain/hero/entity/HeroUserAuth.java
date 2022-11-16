package io.itmca.lifepuzzle.domain.hero.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "user_hero_auth")
public class HeroUserAuth {

    private HeroUserAuth() {};

    public static HeroUserAuth getHeroUserAuthInstance() {
        return new HeroUserAuth();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private Long userNo;
    @ManyToOne
    @JoinColumn(name = "heroNo")
    private Hero hero;

    private String auth;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime updatedAt;

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public void setUserNo(Long userNo) {
        this.userNo = userNo;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Hero getHero() {
        return hero;
    }
}