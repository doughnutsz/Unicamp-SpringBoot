package com.perfectmatch.unicampspringboot.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtils {
    public static ResponseEntity<Map<String, Object>> success(String action) {
        Map<String, Object> map = new HashMap<>();
        map.put("state", true);
        map.put("message", action + " successfully");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    public static ResponseEntity<Map<String, Object>> fail(String message) {
        Map<String, Object> map = new HashMap<>();
        map.put("state", false);
        map.put("message", message);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    public static ResponseEntity<Map<String, Object>> notFound() {
        Map<String, Object> map = new HashMap<>();
        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<Map<String, Object>> unavailable() {
        Map<String, Object> map = new HashMap<>();
        return new ResponseEntity<>(map, HttpStatus.SERVICE_UNAVAILABLE);
    }

    public static ResponseEntity<Map<String, Object>> unauthorized() {
        Map<String, Object> map = new HashMap<>();
        return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
    }

}
