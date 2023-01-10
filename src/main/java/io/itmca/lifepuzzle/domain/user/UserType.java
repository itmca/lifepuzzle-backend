package io.itmca.lifepuzzle.domain.user;

public enum UserType {
    APPLE("apple"), KAKAO("kakao"), GENERAL("general");

    private String name;

    UserType(String name) {
        this.name = name;
    }

    public String frontendKey() {
        return name;
    }
}
