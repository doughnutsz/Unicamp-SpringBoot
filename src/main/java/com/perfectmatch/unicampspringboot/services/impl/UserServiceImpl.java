package com.perfectmatch.unicampspringboot.services.impl;

import com.perfectmatch.unicampspringboot.db.UserDao;
import com.perfectmatch.unicampspringboot.mapper.UserMapper;
import com.perfectmatch.unicampspringboot.services.UserServices;
import com.perfectmatch.unicampspringboot.utils.MyCrypto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserServices {
    @Autowired
    UserMapper userMapper;

    public UserDao UserLogin(String name, String password) {
        UserDao user = GetUserByName(name);

        if (user != null && MyCrypto.matches(password, user.getPassword())) {
            return user;
        } else return null;
    }

    public UserDao GetUserById(Long id) {
        return userMapper.findUserById(id);
    }

    public UserDao GetUserByName(String name) {
        return userMapper.findUserByName(name);
    }

    public void UserRegister(String name, String password) {
        String encoded = MyCrypto.encode(password);
        userMapper.insertUser(name, encoded);
    }

    public void UserProfileUpdate(Long id, String name, String description) {
        userMapper.updateProfile(id.toString(), name, description);
    }

    public void UserPasswordUpdate(Long id, String password) {
        userMapper.updatePassword(id.toString(), MyCrypto.encode(password));
    }
}
