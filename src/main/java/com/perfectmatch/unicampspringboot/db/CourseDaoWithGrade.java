package com.perfectmatch.unicampspringboot.db;

import lombok.*;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CourseDaoWithGrade implements Serializable {
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

    private List<Long> rating_detail = Collections.emptyList();

    public CourseDaoWithGrade(CourseDao dao){
        this.id = dao.getId();
        this.subcategory_id = dao.getSubcategory_id();
        this.name = dao.getName();
        this.provider = dao.getProvider();
        this.difficulty = dao.getDifficulty();
        this.est_hour = dao.getEst_hour();
        this.description = dao.getDescription();
        this.website = dao.getWebsite();
        this.video = dao.getVideo();
        this.assignment = dao.getAssignment();
    }

}
