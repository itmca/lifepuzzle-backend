package io.itmca.lifepuzzle.domain.user.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserEmailValidation {

    @Id
    private Long seq;

    private String email;
}
