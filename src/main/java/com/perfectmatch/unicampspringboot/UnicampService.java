package com.perfectmatch.unicampspringboot;

import com.perfectmatch.unicampspringboot.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnicampService {
    @Autowired
    private UserMapper userMapper;

    public Long userCount(){
        return userMapper.count();
    }
}
