package com.perfectmatch.unicampspringboot.mapper;

import com.perfectmatch.unicampspringboot.db.UserDao;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    Long count();

    UserDao findUserByName(String name);

    UserDao findUserById(Long id);

    void insertUser(String name, String password);

    void updateProfile(String id, String name, String description);

    void updatePassword(String id, String password);
}
