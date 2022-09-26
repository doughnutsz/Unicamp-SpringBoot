package com.perfectmatch.unicampspringboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(value = "com.perfectmatch.unicampspringboot.mapper")
@SpringBootApplication
public class UnicampSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(UnicampSpringBootApplication.class, args);
    }

}
