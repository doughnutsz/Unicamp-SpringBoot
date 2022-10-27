package com.perfectmatch.unicampspringboot.utils;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentView {
    private Long id;
    private Long user_id;
    private Long course_id;
    private Long ref_comment_id;
    private String text;
    private String time;
    private String user_name;
}
