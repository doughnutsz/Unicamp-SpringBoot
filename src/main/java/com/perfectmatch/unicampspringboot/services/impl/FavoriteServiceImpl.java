package com.perfectmatch.unicampspringboot.services.impl;

import com.perfectmatch.unicampspringboot.db.FavoriteDao;
import com.perfectmatch.unicampspringboot.mapper.FavoriteMapper;
import com.perfectmatch.unicampspringboot.services.FavoriteServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteServices {
    @Autowired
    FavoriteMapper favoriteMapper;

    public void updateFavorite(Long user_id, Long course_id, Boolean is_favorite) {
        FavoriteDao dao;
        if (is_favorite) {
            dao = new FavoriteDao();
            dao.setCourse_id(course_id);
            dao.setUser_id(user_id);
            favoriteMapper.insertFavorite(dao);
        } else {
            favoriteMapper.deleteFavorite(user_id.toString(), course_id.toString());
        }
    }

    public boolean IsFavorite(Long user_id, Long course_id) {
        FavoriteDao dao = favoriteMapper.getFavorite(user_id.toString(), course_id.toString());
        return dao != null;
    }

    public List<Long> listUserFavorite(Long user_id) {
        return favoriteMapper.selectFavoriteByUserId(user_id.toString());
    }
}
