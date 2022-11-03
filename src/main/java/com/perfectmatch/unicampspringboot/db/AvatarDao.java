package com.perfectmatch.unicampspringboot.db;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AvatarDao implements Serializable {
    private Long id;
    private String img;
}
