package com.perfectmatch.unicampspringboot.services.impl;

import com.perfectmatch.unicampspringboot.db.CommentDao;
import com.perfectmatch.unicampspringboot.db.UserDao;
import com.perfectmatch.unicampspringboot.mapper.CommentMapper;
import com.perfectmatch.unicampspringboot.mapper.UserMapper;
import com.perfectmatch.unicampspringboot.services.CommentServices;
import com.perfectmatch.unicampspringboot.utils.CommentView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServicesImpl implements CommentServices {
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    UserMapper userMapper;

    public void deleteComment(Long id) {
        commentMapper.eraseCommentText(id.toString(), "");
    }

    public void addComment(Long course_id, Long ref_id, String text, Long user_id) {
        CommentDao dao = new CommentDao();
        dao.setCourse_id(course_id);
        dao.setRef_id(ref_id);
        dao.setText(text);
        dao.setUser_id(user_id);
        commentMapper.insertComment(dao);
    }

    private CommentView CommentViewWrapper(CommentDao it) {
        CommentView v = new CommentView();
        v.setCourse_id(it.getCourse_id());
        v.setId(it.getId());
        v.setText(it.getText());
        v.setRef_comment_id(it.getRef_id());
        v.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(it.getTime()));
        v.setUser_id(it.getUser_id());
        UserDao user = userMapper.findUserById(it.getUser_id());
        v.setUser_name(user.getName());
        return v;
    }

    public List<CommentView> listCommentByCourseId(Long course_id) {
        List<CommentDao> daoList = commentMapper.getCommentByCourseId(course_id.toString());
        List<CommentView> viewList = new ArrayList<>();
        for (CommentDao it : daoList) {
            viewList.add(CommentViewWrapper(it));
        }
        return viewList;
    }

    public CommentDao getCommentById(Long id) {
        return commentMapper.getCommentById(id.toString());
    }
}
