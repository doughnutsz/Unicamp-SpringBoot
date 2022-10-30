package com.perfectmatch.unicampspringboot.controllers;

import com.perfectmatch.unicampspringboot.db.UserDao;
import com.perfectmatch.unicampspringboot.services.AvatarServices;
import com.perfectmatch.unicampspringboot.services.UserServices;
import com.perfectmatch.unicampspringboot.utils.JWTUtils;
import com.perfectmatch.unicampspringboot.utils.ResponseUtils;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller // This means that this class is a Controller
@CrossOrigin
@RequestMapping(path = "/api")
public class AvatarController {
    @Autowired
    UserServices userServices;
    @Autowired
    AvatarServices avatarServices;

    @GetMapping("/avatar/get/{id}")
    ResponseEntity<Map<String, Object>> getAvatar(
            @PathVariable(name = "id") Long id
    ) {
        Map<String, Object> map = new HashMap<>();
        UserDao user = userServices.GetUserById(id);
        if (user == null) {//no such userid
            return ResponseUtils.notFound();
        }
        map.put("img", avatarServices.getAvatar(id));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/avatar/set")
    ResponseEntity<Map<String, Object>> setAvatar(
            @RequestBody Map<String, String> body,
            @RequestHeader String token
    ) {
        long userId;
        try {
            userId = Long.parseLong(JWTUtils.verify(token).getClaim("id").asString());
        } catch (Exception e) {
            return ResponseUtils.unauthorized();
        }
        UserDao user = userServices.GetUserById(userId);
        if (user == null) {//no such userid
            return ResponseUtils.notFound();
        }
        String img = body.get("img");
        avatarServices.setAvatar(userId, img);
        return ResponseUtils.success("set");
    }

}
