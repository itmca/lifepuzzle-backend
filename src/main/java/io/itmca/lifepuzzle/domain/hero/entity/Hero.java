package io.itmca.lifepuzzle.domain.hero.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Hero {

    private Hero(){};
    public static Hero getHeroInstance(){
        return new Hero();
    }
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

//    @OneToMany(targetEntity = HeroValidation.class, mappedBy = "hero", fetch = FetchType.EAGER)
//    private List<HeroValidation> heroValidationList = new ArrayList<>();
//
//    public List<HeroValidation> getHeroValidationList() {
//        return heroValidationList;
//    }


    public void setHeroId(Long heroId) {
        this.heroNo = heroId;
    }

    public void setParentNo(Long parentNo) {
        this.parentNo = parentNo;
    }

    public void setSpouseNo(Long spouseNo) {
        this.spouseNo = spouseNo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getHeroId() {
        return heroNo;
    }

    public Long getParentNo() {
        return parentNo;
    }

    public Long getSpouseNo() {
        return spouseNo;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "Hero{" +
                "heroId=" + heroNo +
                ", parentNo=" + parentNo +
                ", spouseNo=" + spouseNo +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", birthday=" + birthday +
                ", image='" + image + '\'' +
                ", title='" + title + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
