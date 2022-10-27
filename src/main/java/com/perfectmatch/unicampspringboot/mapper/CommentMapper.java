package com.perfectmatch.unicampspringboot.mapper;

import com.perfectmatch.unicampspringboot.db.CommentDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    CommentDao getCommentById(String id);

    void eraseCommentText(String id, String default_text);

    void insertComment(CommentDao dao);

    List<CommentDao> getCommentByCourseId(String course_id);
}
