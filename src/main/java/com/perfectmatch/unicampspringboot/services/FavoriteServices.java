package com.perfectmatch.unicampspringboot.services;

import com.perfectmatch.unicampspringboot.db.FavoriteDao;

import java.util.List;

public interface FavoriteServices {
    void updateFavorite(Long user_id, Long course_id, Boolean is_favorite);

    boolean IsFavorite(Long user_id, Long course_id);

    List<Long> listUserFavorite(Long user_id);
}
