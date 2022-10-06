package com.perfectmatch.unicampspringboot.controllers;

import com.perfectmatch.unicampspringboot.db.CategoryDao;
import com.perfectmatch.unicampspringboot.db.CourseDao;
import com.perfectmatch.unicampspringboot.db.Prerequisite;
import com.perfectmatch.unicampspringboot.services.CategoryServices;
import com.perfectmatch.unicampspringboot.services.CourseServices;
import com.perfectmatch.unicampspringboot.utils.JWTUtils;
import com.perfectmatch.unicampspringboot.utils.MyUtils;
import com.perfectmatch.unicampspringboot.utils.ResponseUtils;
import lombok.*;
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
public class CourseController {
    @Autowired
    CourseServices courseServices;
    @Autowired
    CategoryServices categoryServices;

    static class CourseInsertBody {
        private final Long category_id;
        private final String name;
        private final String provider;
        private final String description;
        private final Integer difficulty;
        private final Integer est_hour;
        private final String website;
        private final String video;
        private final String assignment;

        public CourseInsertBody(Long category_id, String name, String provider,
                                String description, Integer difficulty, Integer est_hour,
                                String website, String video, String assignment) {
            this.category_id = category_id;
            this.name = name;
            this.description = description;
            this.provider = provider;
            this.assignment = assignment;
            this.est_hour = est_hour;
            this.difficulty = difficulty;
            this.website = website;
            this.video = video;
        }
    }

    static class CourseUpdateBody {
        private final Long id;
        private final Long category_id;
        private final String name;
        private final String provider;
        private final String description;
        private final Integer difficulty;
        private final Integer est_hour;
        private final String website;
        private final String video;
        private final String assignment;

        public CourseUpdateBody(Long id, Long category_id, String name, String provider,
                                String description, Integer difficulty, Integer est_hour,
                                String website, String video, String assignment) {
            this.id = id;
            this.category_id = category_id;
            this.name = name;
            this.description = description;
            this.provider = provider;
            this.assignment = assignment;
            this.est_hour = est_hour;
            this.difficulty = difficulty;
            this.website = website;
            this.video = video;
        }
    }

    @PostMapping("/course/add")
    public ResponseEntity<Map<String, Object>> addCourse(
            @RequestBody CourseInsertBody body,
            @RequestHeader String token
    ) {
        if (!JWTUtils.verityAdmin(token)) {
            return ResponseUtils.unavailable();
        }
        CategoryDao category = categoryServices.getCategoryById(body.category_id);
        if (category == null) {
            return ResponseUtils.fail("category not exist");
        }
        courseServices.addCourse(body.category_id, body.name, body.provider,
                body.description, body.difficulty, body.est_hour,
                body.website, body.video, body.assignment);
        return ResponseUtils.success("add");
    }

    @PostMapping("/course/update")
    public ResponseEntity<Map<String, Object>> updateCourse(
            @RequestBody CourseUpdateBody body,
            @RequestHeader String token
    ) {
        if (!JWTUtils.verityAdmin(token)) {
            return ResponseUtils.unavailable();
        }
        CourseDao course = courseServices.getCourseById(body.id);
        if (course == null) {
            return ResponseUtils.fail("there is no such course");
        }
        CategoryDao category = categoryServices.getCategoryById(body.category_id);
        if (category == null) {
            return ResponseUtils.fail("category not exist");
        }
        courseServices.updateCourse(body.id, body.category_id, body.name, body.provider,
                body.description, body.difficulty, body.est_hour,
                body.website, body.video, body.assignment);
        return ResponseUtils.success("update");
    }

    @PostMapping("/course/delete")
    public ResponseEntity<Map<String, Object>> deleteCourse(
            @RequestBody Map<String, Long> body,
            @RequestHeader String token
    ) {

        if (!JWTUtils.verityAdmin(token)) {
            return ResponseUtils.unavailable();
        }
        Long id = body.get("id");
        CourseDao course = courseServices.getCourseById(id);
        if (course == null) {
            return ResponseUtils.fail("there is no such course");
        }
        courseServices.deleteRelatedPrerequisite(id);
        courseServices.deleteCourse(id);
        return ResponseUtils.success("delete");
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    static private class CourseListView {
        private Long id = null;
        private Long category_id = null;
        private String name = null;
        private String provider = null;
        private Integer difficulty = null;
        private Integer est_hour = null;

        public CourseListView(CourseDao dao) {
            this.id = dao.getId();
            this.category_id = dao.getCategory_id();
            this.name = dao.getName();
            this.provider = dao.getProvider();
            this.difficulty = dao.getDifficulty();
            this.est_hour = dao.getEst_hour();
        }
    }

    @GetMapping("/course/list")
    public ResponseEntity<List<CourseListView>> listCourse(
    ) {
        List<CourseDao> courseDaoList = courseServices.listCourse();
        List<CourseListView> list = new ArrayList<>();
        for (CourseDao i : courseDaoList) {
            list.add(new CourseListView(i));
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/course/info/{id}")
    public ResponseEntity<Map<String, Object>> courseInfo(
            @PathVariable(name = "id") Long id
    ) {
        CourseDao courseDao = courseServices.getCourseById(id);
        if (courseDao == null) {
            return ResponseUtils.notFound();
        }
        return new ResponseEntity<>(MyUtils.Object2Map(courseDao), HttpStatus.OK);
    }

    @GetMapping("/course/relation/{id}")
    public ResponseEntity<Map<String, Object>> getRelatedCourse(
            @PathVariable(name = "id") Long id
    ) {
        CourseDao courseDao = courseServices.getCourseById(id);
        if (courseDao == null) {
            return ResponseUtils.notFound();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("pre", courseServices.getPreCourse(id));
        map.put("post", courseServices.getPostCourse(id));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


    static private class RelationBody {
        private final Long pre_id;
        private final Long post_id;

        public RelationBody(Long pre_id, Long post_id) {
            this.post_id = post_id;
            this.pre_id = pre_id;
        }
    }

    @PostMapping("/course/relation/add")
    public ResponseEntity<Map<String, Object>> addRelation(
            @RequestBody RelationBody body,
            @RequestHeader String token
    ) {
        if (!JWTUtils.verityAdmin(token)) {
            return ResponseUtils.unavailable();
        }
        CourseDao pre = courseServices.getCourseById(body.pre_id);
        if (pre == null) {
            return ResponseUtils.fail("no such pre course");
        }
        CourseDao post = courseServices.getCourseById(body.post_id);
        if (post == null) {
            return ResponseUtils.fail("no such post course");
        }
        Prerequisite record = courseServices.getPrerequisite(body.pre_id, body.post_id);
        if (record != null) {
            return ResponseUtils.fail("Prerequisite already exists");
        }
        courseServices.addPrerequisite(body.pre_id, body.post_id);
        return ResponseUtils.success("add");
    }

    @PostMapping("/course/relation/delete")
    public ResponseEntity<Map<String, Object>> deleteRelation(
            @RequestBody RelationBody body,
            @RequestHeader String token
    ) {
        if (!JWTUtils.verityAdmin(token)) {
            return ResponseUtils.unavailable();
        }
        CourseDao pre = courseServices.getCourseById(body.pre_id);
        if (pre == null) {
            return ResponseUtils.fail("no such pre course");
        }
        CourseDao post = courseServices.getCourseById(body.post_id);
        if (post == null) {
            return ResponseUtils.fail("no such post course");
        }

        Prerequisite record = courseServices.getPrerequisite(body.pre_id, body.post_id);
        if (record == null) {
            return ResponseUtils.fail("Prerequisite not exists");
        }
        courseServices.deletePrerequisite(body.pre_id, body.post_id);
        return ResponseUtils.success("delete");
    }
}
