package com.perfectmatch.unicampspringboot.utils;

import com.perfectmatch.unicampspringboot.db.CourseDao;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CourseListView {
    private Long id = null;
    private Long subcategory_id = null;
    private String name = null;
    private String provider = null;
    private Integer difficulty = null;
    private Integer est_hour = null;
    private String description = null;

    public CourseListView(CourseDao dao) {
        this.id = dao.getId();
        this.subcategory_id = dao.getSubcategory_id();
        this.name = dao.getName();
        this.provider = dao.getProvider();
        this.difficulty = dao.getDifficulty();
        this.est_hour = dao.getEst_hour();
        this.description = dao.getDescription();
    }

}
