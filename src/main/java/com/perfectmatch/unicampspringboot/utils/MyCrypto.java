package com.perfectmatch.unicampspringboot.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class MyCrypto {
    private final String salt = "hashed";

    public String encode(String code) {
        return new BCryptPasswordEncoder().encode(code + salt);
    }
}
