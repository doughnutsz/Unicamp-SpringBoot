package com.perfectmatch.unicampspringboot.controllers;

import com.perfectmatch.unicampspringboot.db.CommentDao;
import com.perfectmatch.unicampspringboot.db.CourseDao;
import com.perfectmatch.unicampspringboot.db.UserDao;
import com.perfectmatch.unicampspringboot.services.CommentServices;
import com.perfectmatch.unicampspringboot.services.CourseServices;
import com.perfectmatch.unicampspringboot.services.UserServices;
import com.perfectmatch.unicampspringboot.utils.CommentView;
import com.perfectmatch.unicampspringboot.utils.JWTUtils;
import com.perfectmatch.unicampspringboot.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller // This means that this class is a Controller
@CrossOrigin
@RequestMapping(path = "/api")
public class CommentController {
    @Autowired
    CommentServices commentServices;
    @Autowired
    CourseServices courseServices;
    @Autowired
    UserServices userServices;

    @GetMapping("/comment/get/{course_id}")
    ResponseEntity<List<CommentView>> ListComment(
            @PathVariable(name = "course_id") Long course_id
    ) {
        List<CommentView> ls = new ArrayList<>();
        CourseDao course = courseServices.getCourseById(course_id);
        if (course == null) {//no such course
            return new ResponseEntity<>(ls, HttpStatus.NOT_FOUND);
        }
        ls = commentServices.listCommentByCourseId(course_id);
        return new ResponseEntity<>(ls, HttpStatus.OK);
    }

    @PostMapping("/comment/delete")
    ResponseEntity<Map<String, Object>> DeleteComment(
            @RequestBody Map<String, Long> body,
            @RequestHeader String token
    ) {
        if (!JWTUtils.verityAdmin(token)) {
            return ResponseUtils.unavailable();
        }
        Long id = body.get("id");
        if (commentServices.getCommentById(id) == null) {
            return ResponseUtils.notFound();
        }
        commentServices.deleteComment(id);
        return ResponseUtils.success("delete");
    }

    static class InsertCommentBody {
        private final Long course_id;
        private final Long ref_id;
        private final String text;

        public InsertCommentBody(Long course_id, Long ref_comment_id, String text) {
            this.course_id = course_id;
            this.ref_id = ref_comment_id;
            this.text = text;
        }
    }

    @PostMapping("/comment/add")
    ResponseEntity<Map<String, Object>> AddComment(
            @RequestBody InsertCommentBody body,
            @RequestHeader String token
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

        if (body.ref_id != 0L) {
            CommentDao dao = commentServices.getCommentById(body.ref_id);
            if (dao == null || !body.course_id.equals(dao.getCourse_id())) {
                return ResponseUtils.notFound();//illegal ref
            }
        }
        commentServices.addComment(body.course_id, body.ref_id, body.text, userId);
        return ResponseUtils.success("add");
    }

}
