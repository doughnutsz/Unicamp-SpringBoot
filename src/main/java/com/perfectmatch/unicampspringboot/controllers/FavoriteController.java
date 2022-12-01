package com.perfectmatch.unicampspringboot.controllers;

import com.perfectmatch.unicampspringboot.db.CourseDao;
import com.perfectmatch.unicampspringboot.db.CourseRecDao;
import com.perfectmatch.unicampspringboot.db.UserDao;
import com.perfectmatch.unicampspringboot.services.CourseServices;
import com.perfectmatch.unicampspringboot.services.FavoriteServices;
import com.perfectmatch.unicampspringboot.services.GradeServices;
import com.perfectmatch.unicampspringboot.services.UserServices;
import com.perfectmatch.unicampspringboot.utils.JWTUtils;
import com.perfectmatch.unicampspringboot.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller // This means that this class is a Controller
@CrossOrigin
@RequestMapping(path = "/api")
public class FavoriteController {
    @Autowired
    FavoriteServices favoriteServices;
    @Autowired
    UserServices userServices;
    @Autowired
    CourseServices courseServices;

    @Autowired
    GradeServices gradeServices;

    @GetMapping("/favorite")
    public ResponseEntity<List<CourseRecDao>> listFavoriteCourse(
            @RequestHeader(value = "token") String token
    ) {
        long id;
        List<CourseRecDao> ls = new ArrayList<>();
        try {
            id = Long.parseLong(JWTUtils.verify(token).getClaim("id").asString());
        } catch (Exception e) {
            return new ResponseEntity<>(ls, HttpStatus.UNAUTHORIZED);
        }
        UserDao user = userServices.GetUserById(id);
        if (user == null) {//no such userid
            return new ResponseEntity<>(ls, HttpStatus.NOT_FOUND);
        }
        List<Long> courseIdList = favoriteServices.listUserFavorite(id);
        for (long courseId : courseIdList) {
            ls.add(new CourseRecDao(courseServices.getCourseById(courseId)));
        }
        ls.forEach((CourseRecDao c) -> c.setRatings(gradeServices.getGradeDetail(c.getId())));
        return new ResponseEntity<>(ls, HttpStatus.OK);
    }

    @GetMapping("/favorite/{id}")
    public ResponseEntity<Map<String, Object>> checkFavoriteCourse(
            @RequestHeader(value = "token") String token,
            @PathVariable(name = "id") Long id
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
        CourseDao course = courseServices.getCourseById(id);
        if (course == null) {//no such course
            return ResponseUtils.notFound();
        }
        boolean is_favorite = favoriteServices.IsFavorite(userId, id);
        Map<String, Object> map = new HashMap<>();
        map.put("is_favorite", is_favorite);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    static class setFavoriteBody {
        private final Long course_id;
        private final boolean is_favorite;

        public setFavoriteBody(Long course_id, boolean is_favorite) {
            this.course_id = course_id;
            this.is_favorite = is_favorite;
        }
    }

    @PostMapping("/favorite/set")
    public ResponseEntity<Map<String, Object>> checkFavoriteCourse(
            @RequestHeader(value = "token") String token,
            @RequestBody setFavoriteBody body
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
        favoriteServices.updateFavorite(userId, body.course_id, body.is_favorite);
        return ResponseUtils.success("set");
    }

}
