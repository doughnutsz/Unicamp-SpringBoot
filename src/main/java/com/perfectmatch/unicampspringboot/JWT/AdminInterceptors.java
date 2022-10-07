package com.perfectmatch.unicampspringboot.JWT;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.perfectmatch.unicampspringboot.exception.ExceptionEnum;
import com.perfectmatch.unicampspringboot.exception.MyException;
import com.perfectmatch.unicampspringboot.utils.JWTUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminInterceptors implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().toUpperCase().equals("OPTIONS"))
            return true; // 通过所有OPTION请求
        // 获取请求头中令牌
        String token = request.getHeader("token");
        DecodedJWT jwt = JWTUtils.verify(token);
        try {
            if (jwt.getClaim("type").asString().equals("admin")) return true;
        } catch (Exception e) {
            ;
        }
        throw new MyException(ExceptionEnum.PERMISSION_DENIED);
    }
}
