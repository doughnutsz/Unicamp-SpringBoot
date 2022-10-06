package com.perfectmatch.unicampspringboot.mapper;

import com.perfectmatch.unicampspringboot.db.CourseDao;
import com.perfectmatch.unicampspringboot.db.Prerequisite;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseMapper {
    CourseDao findCourseById(Long id);

    void insertCourse(String category_id, String name, String provider,
                      String description, String difficulty, String est_hour,
                      String website, String video, String assignment);

    void updateCourse(String id, String category_id, String name, String provider,
                      String description, String difficulty, String est_hour,
                      String website, String video, String assignment);

    void deleteCourse(String id);

    List<CourseDao> listCourse();

    List<Long> getPreCourse(String id);

    List<Long> getPostCourse(String id);

    Prerequisite getPrerequisite(String pre_id, String post_id);

    void addPrerequisite(String pre_id, String post_id);

    void deletePrerequisite(String pre_id, String post_id);

    void deleteRelatedPrerequisite(String id);


}
