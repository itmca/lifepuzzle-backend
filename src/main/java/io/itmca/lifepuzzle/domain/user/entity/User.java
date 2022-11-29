package io.itmca.lifepuzzle.domain.user.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    private Long userNo;
}
