package com.perfectmatch.unicampspringboot.db;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Prerequisite {
    private Long id;
    private Long preId;
    private Long postId;
}
