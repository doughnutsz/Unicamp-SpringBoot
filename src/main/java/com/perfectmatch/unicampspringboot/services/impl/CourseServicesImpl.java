package com.perfectmatch.unicampspringboot.services.impl;

import com.perfectmatch.unicampspringboot.db.CourseDao;
import com.perfectmatch.unicampspringboot.db.Prerequisite;
import com.perfectmatch.unicampspringboot.mapper.CourseMapper;
import com.perfectmatch.unicampspringboot.services.CourseServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServicesImpl implements CourseServices {
    @Autowired
    CourseMapper courseMapper;

    public CourseDao getCourseById(Long id) {
        return courseMapper.findCourseById(id);
    }

    public void addCourse(Long subcategory_id, String name, String provider,
                          String description, Integer difficulty, Integer est_hour,
                          String website, String video, String assignment) {
        courseMapper.insertCourse(subcategory_id.toString(), name, provider,
                description, difficulty.toString(),
                est_hour.toString(), website, video, assignment);
    }

    public void updateCourse(Long id, Long subcategory_id, String name, String provider,
                             String description, Integer difficulty, Integer est_hour,
                             String website, String video, String assignment) {
        courseMapper.updateCourse(id.toString(), subcategory_id.toString(), name,
                provider, description, difficulty.toString(),
                est_hour.toString(), website, video, assignment);
    }

    public void deleteCourse(Long id) {
        courseMapper.deleteCourse(id.toString());
    }

    public List<CourseDao> listCourse() {
        return courseMapper.listCourse();
    }

    public List<Long> getPreCourse(Long id) {
        return courseMapper.getPreCourse(id.toString());
    }


    public List<Long> getPostCourse(Long id) {
        return courseMapper.getPostCourse(id.toString());
    }

    public Prerequisite getPrerequisite(Long pre_id, Long post_id) {
        return courseMapper.getPrerequisite(pre_id.toString(), post_id.toString());
    }

    public void addPrerequisite(Long pre_id, Long post_id) {
        courseMapper.addPrerequisite(pre_id.toString(), post_id.toString());
    }

    public void deletePrerequisite(Long pre_id, Long post_id) {
        courseMapper.deletePrerequisite(pre_id.toString(), post_id.toString());
    }

    public void deleteRelatedPrerequisite(Long id) {
        courseMapper.deleteRelatedPrerequisite(id.toString());
    }
}
