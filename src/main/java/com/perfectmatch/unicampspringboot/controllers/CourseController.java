package com.perfectmatch.unicampspringboot.controllers;

import com.perfectmatch.unicampspringboot.db.*;
import com.perfectmatch.unicampspringboot.services.CategoryServices;
import com.perfectmatch.unicampspringboot.services.CourseServices;
import com.perfectmatch.unicampspringboot.utils.JWTUtils;
import com.perfectmatch.unicampspringboot.utils.MyUtils;
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
public class CourseController {
    @Autowired
    CourseServices courseServices;
    @Autowired
    CategoryServices categoryServices;

    static class CourseInsertBody {
        private final Long subcategory_id;
        private final String name;
        private final String provider;
        private final String description;
        private final Integer difficulty;
        private final Integer est_hour;
        private final String website;
        private final String video;
        private final String assignment;

        public CourseInsertBody(Long subcategory_id, String name, String provider,
                                String description, Integer difficulty, Integer est_hour,
                                String website, String video, String assignment) {
            this.subcategory_id = subcategory_id;
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
        private final Long subcategory_id;
        private final String name;
        private final String provider;
        private final String description;
        private final Integer difficulty;
        private final Integer est_hour;
        private final String website;
        private final String video;
        private final String assignment;

        public CourseUpdateBody(Long id, Long subcategory_id, String name, String provider,
                                String description, Integer difficulty, Integer est_hour,
                                String website, String video, String assignment) {
            this.id = id;
            this.subcategory_id = subcategory_id;
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
        SubCategoryDao category = categoryServices.getSubCategoryById(body.subcategory_id);
        if (category == null) {
            return ResponseUtils.fail("category not exist");
        }
        courseServices.addCourse(body.subcategory_id, body.name, body.provider,
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
        SubCategoryDao category = categoryServices.getSubCategoryById(body.subcategory_id);
        if (category == null) {
            return ResponseUtils.fail("category not exist");
        }
        courseServices.updateCourse(body.id, body.subcategory_id, body.name, body.provider,
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



    @GetMapping("/course/list")
    public ResponseEntity<List<CourseRecDao>> listCourse(
    ) {
        List<CourseDao> courseDaoList = courseServices.listCourse();
        List<CourseRecDao> list = new ArrayList<>();
        for (CourseDao i : courseDaoList) {
            list.add(new CourseRecDao(i));
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/course/info/{id}")
    public ResponseEntity<Map<String, Object>> courseInfo(
            @PathVariable(name = "id") Long id
    ) {
        CourseDaoWithGrade courseDaoWithGrade = courseServices.getCourseWithGradeById(id);
        if (courseDaoWithGrade == null) {
            return ResponseUtils.notFound();
        }
        return new ResponseEntity<>(MyUtils.Object2Map(courseDaoWithGrade), HttpStatus.OK);
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

    @GetMapping("/recommend/new/main")
    public ResponseEntity<List<CourseRecDao>> listNewCourse(
    ) {
        List<CourseRecDao> courseRecDaoList = courseServices.listNew();
        return new ResponseEntity<>(courseRecDaoList, HttpStatus.OK);
    }

    @GetMapping("/recommend/hot/main")
    public ResponseEntity<List<CourseRecDao>> listHotCourse(
    ) {
        List<CourseRecDao> courseRecDaoList = courseServices.listHot();
        return new ResponseEntity<>(courseRecDaoList, HttpStatus.OK);
    }

    @GetMapping("/recommend/rec/main")
    public ResponseEntity<List<CourseRecDao>> listRecCourse(
            @RequestHeader String token
    ) throws Exception {
        Long userId = Long.parseLong(JWTUtils.verify(token).getClaim("id").asString());
        List<CourseRecDao> courseRecDaoList = courseServices.listRec(userId);
        return new ResponseEntity<>(courseRecDaoList, HttpStatus.OK);
    }

    @GetMapping("/recommend/related/{id}")
    public ResponseEntity<List<CourseRecDao>> listRecCourse(
            @PathVariable(name = "id") Long id
    ) throws Exception {
        List<CourseRecDao> courseRecDaoList = courseServices.listRelated(id);
        return new ResponseEntity<>(courseRecDaoList, HttpStatus.OK);
    }

    @PostMapping("/course/card")
    public ResponseEntity<List<CourseDaoWithGrade>> getCard(
            @RequestBody Map<String, Object> body
    ) {
        List<CourseDaoWithGrade> list = courseServices.getCard(body);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}
