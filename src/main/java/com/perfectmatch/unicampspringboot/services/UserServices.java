package com.perfectmatch.unicampspringboot.services;

import com.perfectmatch.unicampspringboot.db.UserDao;

public interface UserServices {
    UserDao UserLogin(String name, String password);

    UserDao GetUserById(Long id);

    UserDao GetUserByName(String name);

    void UserRegister(String name, String password);

    void UserProfileUpdate(Long id, String name, String description);

    void UserPasswordUpdate(Long id, String password);
}
