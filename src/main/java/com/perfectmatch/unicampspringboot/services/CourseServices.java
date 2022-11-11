package com.perfectmatch.unicampspringboot.services;

import com.perfectmatch.unicampspringboot.db.CourseDao;
import com.perfectmatch.unicampspringboot.db.CourseRecDao;
import com.perfectmatch.unicampspringboot.db.Prerequisite;
import org.apache.mahout.cf.taste.common.TasteException;

import java.util.List;

public interface CourseServices {
    CourseDao getCourseById(Long id);

    void addCourse(Long subcategory_id, String name, String provider,
                   String description, Integer difficulty, Integer est_hour,
                   String website, String video, String assignment);

    void updateCourse(Long id, Long subcategory_id, String name, String provider,
                      String description, Integer difficulty, Integer est_hour,
                      String website, String video, String assignment);

    void deleteCourse(Long id);

    List<CourseDao> listCourse();

    List<CourseRecDao> getPreCourse(Long id);

    List<CourseRecDao> getPostCourse(Long id);

    Prerequisite getPrerequisite(Long pre_id, Long post_id);

    void addPrerequisite(Long pre_id, Long post_id);

    void deletePrerequisite(Long pre_id, Long post_id);

    void deleteRelatedPrerequisite(Long id);

    List<CourseRecDao> listNew();

    List<CourseRecDao> listHot();

    List<CourseRecDao> listRec(Long userId) throws TasteException;

    List<CourseRecDao> listRelated(Long courseId) throws TasteException;
}
