package com.perfectmatch.unicampspringboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = MyException.class)
    public ResponseEntity<Map<String, Object>> exceptionHandler(MyException e) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", e.getErrorMsg());
        map.put("state", false);
        return new ResponseEntity<>(map, e.getCode());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Map<String, Object>> exceptionHandler(Exception e) {
        Map<String, Object> map = Collections.unmodifiableMap(new HashMap<String, Object>());
        map.put("message", "unknown error!");
        System.out.println(e);
        return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}