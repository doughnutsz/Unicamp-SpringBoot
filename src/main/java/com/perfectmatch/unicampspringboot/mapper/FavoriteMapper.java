package com.perfectmatch.unicampspringboot.mapper;

import com.perfectmatch.unicampspringboot.db.FavoriteDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FavoriteMapper {
    FavoriteDao getFavorite(String user_id, String course_id);

    void insertFavorite(FavoriteDao dao);

    void deleteFavorite(String user_id, String course_id);

    List<Long> selectFavoriteByUserId(String user_id);

    List<FavoriteDao> getAll();

    List<FavoriteDao> selectFavoriteByUserIds(@Param("user_ids") List<String> user_ids);

}
