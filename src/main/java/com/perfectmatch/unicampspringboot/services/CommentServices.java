package com.perfectmatch.unicampspringboot.services;

import com.perfectmatch.unicampspringboot.db.CommentDao;
import com.perfectmatch.unicampspringboot.utils.CommentView;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public interface CommentServices {
    void deleteComment(Long id);

    void addComment(Long course_id, Long ref_id, String text, Long user_id);

    List<CommentView> listCommentByCourseId(Long course_id);

    CommentDao getCommentById(Long id);
}
