package com.perfectmatch.unicampspringboot.JWT;

import com.perfectmatch.unicampspringboot.exception.ExceptionEnum;
import com.perfectmatch.unicampspringboot.exception.MyException;
import com.perfectmatch.unicampspringboot.utils.JWTUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JWTInterceptors implements HandlerInterceptor {
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
        if(request.getMethod().toUpperCase().equals("OPTIONS"))
            return true; // 通过所有OPTION请求
        // 获取请求头中令牌
        try {
            String token = request.getHeader("token");
            // 验证令牌
            JWTUtils.verify(token);
            return true;

        }catch (Exception e){
            ;
        }
        throw new MyException(ExceptionEnum.INVALID_TOKEN);
    }
}
