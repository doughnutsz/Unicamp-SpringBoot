package com.perfectmatch.unicampspringboot.db;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CourseRecDao implements Serializable {
    private Long id = null;
    private Long subcategory_id = null;
    private String name = null;
    private String provider = null;
    private Integer difficulty = null;
    private Integer est_hour = null;
    private String description = null;

    private List<Long> ratings;

    public CourseRecDao(CourseDao dao) {
        this.id = dao.getId();
        this.subcategory_id = dao.getSubcategory_id();
        this.name = dao.getName();
        this.provider = dao.getProvider();
        this.difficulty = dao.getDifficulty();
        this.est_hour = dao.getEst_hour();
        this.description = dao.getDescription();
    }
}
