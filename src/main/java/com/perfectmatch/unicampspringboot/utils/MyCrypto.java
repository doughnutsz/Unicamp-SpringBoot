package com.perfectmatch.unicampspringboot.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class MyCrypto {
    private static final String salt = "hashed";

    public static String encode(String code) {
        return new BCryptPasswordEncoder().encode(code + salt);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return new BCryptPasswordEncoder().matches(rawPassword + salt, encodedPassword);
    }
}
