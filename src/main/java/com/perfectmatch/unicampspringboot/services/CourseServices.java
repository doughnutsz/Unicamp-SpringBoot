package com.perfectmatch.unicampspringboot.services;

import com.perfectmatch.unicampspringboot.db.CourseDao;
import com.perfectmatch.unicampspringboot.db.Prerequisite;

import java.util.List;

public interface CourseServices {
    CourseDao getCourseById(Long id);

    void addCourse(Long category_id, String name, String provider,
                   String description, Integer difficulty, Integer est_hour,
                   String website, String video, String assignment);

    void updateCourse(Long id, Long category_id, String name, String provider,
                      String description, Integer difficulty, Integer est_hour,
                      String website, String video, String assignment);

    void deleteCourse(Long id);

    List<CourseDao> listCourse();

    List<Long> getPreCourse(Long id);

    List<Long> getPostCourse(Long id);

    Prerequisite getPrerequisite(Long pre_id, Long post_id);

    void addPrerequisite(Long pre_id, Long post_id);

    void deletePrerequisite(Long pre_id, Long post_id);

    void deleteRelatedPrerequisite(Long id);

}
