package com.perfectmatch.unicampspringboot.exception;

import org.springframework.http.HttpStatus;

public class MyException extends RuntimeException{
    private ExceptionEnum exceptionEnum;
    private HttpStatus code;
    private String errorMsg;

    public MyException() {
        super();
    }

    public MyException(ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
        this.code = exceptionEnum.getCode();
        this.errorMsg = exceptionEnum.getMsg();
    }
    public MyException(ExceptionEnum exceptionEnum,String msg) {
        this.exceptionEnum = exceptionEnum;
        this.code = exceptionEnum.getCode();
        this.errorMsg = exceptionEnum.getMsg()+msg;
    }

    public ExceptionEnum getExceptionEnum() {
        return exceptionEnum;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public HttpStatus getCode() {
        return code;
    }

    public void setCode(HttpStatus code) {
        this.code = code;
    }
}
