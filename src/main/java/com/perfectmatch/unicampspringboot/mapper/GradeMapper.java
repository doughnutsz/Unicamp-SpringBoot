package com.perfectmatch.unicampspringboot.mapper;

import com.perfectmatch.unicampspringboot.db.GradeDao;
import com.perfectmatch.unicampspringboot.utils.GradeDetailView;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GradeMapper {
    GradeDao getGrade(String user_id, String course_id);

    void insertGrade(String user_id, String course_id, String rating);

    void updateGrade(String user_id, String course_id, String rating);

    List<GradeDetailView> gradeDetail(String course_id);
}
