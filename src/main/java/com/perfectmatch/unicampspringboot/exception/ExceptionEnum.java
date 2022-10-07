package com.perfectmatch.unicampspringboot.exception;

import org.springframework.http.HttpStatus;

public enum ExceptionEnum {
    INVALID_TOKEN(HttpStatus.OK, "invalid token"),
    PERMISSION_DENIED(HttpStatus.OK,"permission denied");
    private HttpStatus code;
    private String msg;
    ExceptionEnum(HttpStatus code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public HttpStatus getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
}
