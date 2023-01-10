package io.itmca.lifepuzzle.domain.auth;

public enum TokenType {
    ACCESS("access"), REFRESH("refresh");

    private String name;

    TokenType(String name) {
        this.name = name;
    }

    public String token() {
        return name;
    }
}
