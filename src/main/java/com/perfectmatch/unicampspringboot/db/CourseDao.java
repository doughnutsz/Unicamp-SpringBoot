package com.perfectmatch.unicampspringboot.db;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CourseDao implements Serializable {
    private Long id;
    private Long subcategory_id;
    private String name;
    private String provider;
    private String description;
    private Integer difficulty;
    private Integer est_hour;
    private String website;
    private String video;
    private String assignment;
}
