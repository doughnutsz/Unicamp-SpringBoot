package com.perfectmatch.unicampspringboot.controllers;

import com.perfectmatch.unicampspringboot.db.UserDao;
import com.perfectmatch.unicampspringboot.services.UserServices;
import com.perfectmatch.unicampspringboot.utils.JWTUtils;
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
public class UserController {
    @Autowired
    UserServices userServices;

    static class UserLoginBody {
        private final String name;
        private final String password;

        public UserLoginBody(String name, String password) {
            this.name = name;
            this.password = password;
        }

    }

    static class UserProfileBody {

        private final String name;
        private final String description;

        public UserProfileBody(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }

    static class ResetPasswordBody {
        private final String oldPassword;
        private final String newPassword;

        public ResetPasswordBody(String oldPassword, String newPassword) {
            this.oldPassword = oldPassword;
            this.newPassword = newPassword;
        }

    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @RequestBody UserLoginBody body
    ) {
        Map<String, Object> map = new HashMap<>();
        UserDao sameUser = userServices.GetUserByName(body.name);
        if (sameUser != null) {//make sure name is unique
            map.put("state", false);
            map.put("message", "The username is already taken");
            map.put("token", "");
        } else {
            userServices.UserRegister(body.name, body.password);
            UserDao user = userServices.UserLogin(body.name, body.password);
            String token = JWTUtils.getLoginToken((user.getId()).toString(), "");
            map.put("state", true);
            map.put("message", "register successfully");
            map.put("token", token);
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody UserLoginBody body
    ) {
        Map<String, Object> map = new HashMap<>();
        if (userServices.GetUserByName(body.name) == null) {//no such username
            map.put("state", false);
            map.put("message", "name and password not match");
            map.put("token", "");
        } else {
            UserDao user = userServices.UserLogin(body.name, body.password);
            if (user == null) {//wrong password maybe
                map.put("state", false);
                map.put("message", "name and password not match");
                map.put("token", "");
            } else {
                String token = JWTUtils.getLoginToken((user.getId()).toString(), user.getIs_admin() ? "admin" : "");
                map.put("state", true);
                map.put("message", "login successfully");
                map.put("token", token);
            }
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<Map<String, Object>> getProfile(
            @PathVariable(name = "id") Long id
    ) {
        Map<String, Object> map = new HashMap<>();
        UserDao user = userServices.GetUserById(id);
        if (user == null) {//no such userid
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }
        map.put("id", user.getId());
        map.put("name", user.getName());
        map.put("description", user.getDescription());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/profile/update")
    public ResponseEntity<Map<String, Object>> updateProfile(
            @RequestHeader(value = "token") String token,
            @RequestBody UserProfileBody body
    ) {
        Map<String, Object> map = new HashMap<>();
        Long id;
        try {
            id = Long.parseLong(JWTUtils.verify(token).getClaim("id").asString());
        } catch (Exception e) {
            return new ResponseEntity<>(map, HttpStatus.SERVICE_UNAVAILABLE);
        }


        UserDao user = userServices.GetUserById(id);
        if (user == null) {//no such userid
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }
        UserDao sameNameUser = userServices.GetUserByName(body.name);
        if (sameNameUser != null && !sameNameUser.getId().equals(user.getId())) {//use a taken name(not himself)
            map.put("state", false);
            map.put("message", "The username is already taken");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        userServices.UserProfileUpdate(id, body.name, body.description);
        map.put("state", true);
        map.put("message", "modify successfully");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/reset/password")
    public ResponseEntity<Map<String, Object>> updateProfile(
            @RequestHeader(value = "token") String token,
            @RequestBody ResetPasswordBody body
    ) {
        Map<String, Object> map = new HashMap<>();
        Long id;
        try {
            id = Long.parseLong(JWTUtils.verify(token).getClaim("id").asString());
        } catch (Exception e) {
            return new ResponseEntity<>(map, HttpStatus.SERVICE_UNAVAILABLE);
        }
        UserDao user = userServices.GetUserById(id);
        if (user == null) {
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }
        UserDao checkLoginUser = userServices.UserLogin(user.getName(), body.oldPassword);
        if (checkLoginUser == null) {
            map.put("state", false);
            map.put("message", "old password is incorrect");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        userServices.UserPasswordUpdate(id, body.newPassword);
        map.put("state", true);
        map.put("message", "modify successfully");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


}
