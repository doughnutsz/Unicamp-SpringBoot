package com.perfectmatch.unicampspringboot.db;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GradeDao {
    private Long id;
    private Long user_id;
    private Long course_id;
    private Long rating;
}
