package com.perfectmatch.unicampspringboot.mapper;

import com.perfectmatch.unicampspringboot.db.AvatarDao;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AvatarMapper {
    AvatarDao getAvatarById(String id);

    void insertAvatar(String id, String img);

    void updateAvatar(String id, String img);
}
