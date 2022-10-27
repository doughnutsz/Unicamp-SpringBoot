package com.perfectmatch.unicampspringboot.db;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDao implements Serializable {
    private Long id;
    private String name;
    private Boolean is_admin;
    private String password;
    private String description;
}
