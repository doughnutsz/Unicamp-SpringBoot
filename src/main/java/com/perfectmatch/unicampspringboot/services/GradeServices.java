package com.perfectmatch.unicampspringboot.services;

import com.perfectmatch.unicampspringboot.db.GradeDao;

import java.util.List;

public interface GradeServices {
    GradeDao getMyGrade(Long user_id, Long course_id);

    List<Long> getGradeDetail(Long course_id);

    void setMyGrade(Long user_id, Long course_id, Long rating);
}
