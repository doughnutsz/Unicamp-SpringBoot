package com.perfectmatch.unicampspringboot.services.impl;

import com.perfectmatch.unicampspringboot.db.GradeDao;
import com.perfectmatch.unicampspringboot.mapper.GradeMapper;
import com.perfectmatch.unicampspringboot.services.GradeServices;
import com.perfectmatch.unicampspringboot.utils.GradeDetailView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class GradeServiceImpl implements GradeServices {
    @Autowired
    GradeMapper gradeMapper;

    public GradeDao getMyGrade(Long user_id, Long course_id) {
        return gradeMapper.getGrade(user_id.toString(), course_id.toString());
    }

    public List<Long> getGradeDetail(Long course_id) {
        Long[] ls = new Long[5];
        for (int i = 0; i < 5; i++)
            ls[i] = 0L;
        List<GradeDetailView> view = gradeMapper.gradeDetail(course_id.toString());
        for (GradeDetailView it : view) {
            ls[Math.toIntExact(it.getRating() - 1)] = it.getCount();
        }
        if(view.isEmpty()){
            ls = new Long[]{0L, 0L, 0L, 0L, 0L};
        }
        return Arrays.asList(ls);
    }

    public void setMyGrade(Long user_id, Long course_id, Long rating) {
        if (gradeMapper.getGrade(user_id.toString(), course_id.toString()) != null) {
            gradeMapper.updateGrade(user_id.toString(), course_id.toString(), rating.toString());
        } else {
            gradeMapper.insertGrade(user_id.toString(), course_id.toString(), rating.toString());
        }
    }
}
