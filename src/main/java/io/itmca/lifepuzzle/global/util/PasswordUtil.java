package io.itmca.lifepuzzle.global.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Random;

public class PasswordUtil {

    public static String hashPassword(String password, String salt) {
        var bcryptSalt = BCrypt.gensalt();
        return BCrypt.hashpw(password + salt, bcryptSalt);
    }

    public static String genSalt() {
        var bytes = new byte[8];
        new Random().nextBytes(bytes);

        var result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
