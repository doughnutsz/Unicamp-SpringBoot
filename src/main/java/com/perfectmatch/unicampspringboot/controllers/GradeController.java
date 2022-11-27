package com.perfectmatch.unicampspringboot.controllers;

import com.perfectmatch.unicampspringboot.db.CourseDao;
import com.perfectmatch.unicampspringboot.db.GradeDao;
import com.perfectmatch.unicampspringboot.db.UserDao;
import com.perfectmatch.unicampspringboot.services.CourseServices;
import com.perfectmatch.unicampspringboot.services.GradeServices;
import com.perfectmatch.unicampspringboot.services.UserServices;
import com.perfectmatch.unicampspringboot.utils.JWTUtils;
import com.perfectmatch.unicampspringboot.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller // This means that this class is a Controller
@CrossOrigin
@RequestMapping(path = "/api")
public class GradeController {
    @Autowired
    GradeServices gradeServices;
    @Autowired
    UserServices userServices;
    @Autowired
    CourseServices courseServices;

    @GetMapping("/grade/get/{course_id}")
    ResponseEntity<Map<String, Object>> getMyGrade(
            @RequestHeader(value = "token") String token,
            @PathVariable(name = "course_id") Long course_id
    ) {
        long userId;
        try {
            userId = Long.parseLong(JWTUtils.verify(token).getClaim("id").asString());
        } catch (Exception e) {
            return ResponseUtils.unauthorized();
        }
        UserDao user = userServices.GetUserById(userId);
        if (user == null) {//no such userid
            return ResponseUtils.notFound();
        }
        CourseDao course = courseServices.getCourseById(course_id);
        if (course == null) {//no such course
            return ResponseUtils.notFound();
        }
        Map<String, Object> map = new HashMap<>();
        Long myRating = 0L;
        GradeDao dao = gradeServices.getMyGrade(userId, course_id);
        if (dao != null) myRating = dao.getRating();
        map.put("my_rating", myRating);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/grade/detail/{course_id}")
    ResponseEntity<Map<String, Object>> GradeDetail(
            @PathVariable(name = "course_id") Long course_id
    ) {
        CourseDao course = courseServices.getCourseById(course_id);
        if (course == null) {//no such course
            return ResponseUtils.notFound();
        }
        Map<String, Object> map = new HashMap<>();
        List<Long> rating_detail = gradeServices.getGradeDetail(course_id);
        map.put("rating_detail", rating_detail);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


    static class setGradeBody {
        private final Long course_id;
        private final Long rating;

        public setGradeBody(Long course_id, Long rating) {
            this.course_id = course_id;
            this.rating = rating;
        }
    }

    @PostMapping("/grade/set")
    ResponseEntity<Map<String, Object>> SetGrade(
            @RequestHeader(value = "token") String token,
            @RequestBody setGradeBody body
    ) {
        long userId;
        try {
            userId = Long.parseLong(JWTUtils.verify(token).getClaim("id").asString());
        } catch (Exception e) {
            return ResponseUtils.unauthorized();
        }
        UserDao user = userServices.GetUserById(userId);
        if (user == null) {//no such userid
            return ResponseUtils.notFound();
        }
        CourseDao course = courseServices.getCourseById(body.course_id);
        if (course == null) {//no such course
            return ResponseUtils.notFound();
        }
        if (body.rating < 1 || body.rating > 5) {
            return ResponseUtils.fail("illegal rating range");
        }
        gradeServices.setMyGrade(userId, body.course_id, body.rating);
        return ResponseUtils.success("set");
    }

}
