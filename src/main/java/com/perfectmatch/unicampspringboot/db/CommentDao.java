package com.perfectmatch.unicampspringboot.db;

import lombok.*;


import java.io.Serializable;
import java.sql.Timestamp;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentDao implements Serializable {
    private Long id;
    private Long user_id;
    private Long course_id;
    private Long ref_id;
    private String text;
    private Timestamp time;
}