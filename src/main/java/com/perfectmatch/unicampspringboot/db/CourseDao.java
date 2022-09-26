package com.perfectmatch.unicampspringboot.db;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CourseDao {
    private Long id;
    private Long categoryId;
    private String name;
    private String provider;
    private String description;
    private Integer difficulty;
    private Integer estHour;
    private String website;
    private String video;
    private String assignment;
}
