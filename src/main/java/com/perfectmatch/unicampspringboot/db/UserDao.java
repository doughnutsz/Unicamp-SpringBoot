package com.perfectmatch.unicampspringboot.db;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDao {
    private Long id;
    private String name;
    private Boolean isAdmin = false;
    private String password;
    private String description;
}
