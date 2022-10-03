package com.perfectmatch.unicampspringboot.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {
    /**
     * 生成token  header.payload.signature
     */
    private static final String SIGN = "maze";


    public static String getToken(Map<String, String> map) {

        Calendar instance = Calendar.getInstance();
        // 默认7天过期
        instance.add(Calendar.DATE, 7);

        //创建jwt builder
        JWTCreator.Builder builder = JWT.create();

        // payload
        map.forEach((k, v) -> builder.withClaim(k, v));

        String token = builder.withExpiresAt(instance.getTime())  //指定令牌过期时间
                .sign(Algorithm.HMAC256(SIGN));  // sign
        return token;
    }

    /**
     * 验证token合法性,获取token信息方法
     */
    public static DecodedJWT verify(String token) {
        return JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
    }

    public static String getLoginToken(String id, String type) {
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("id", id);
        payload.put("type", type);
        String token = JWTUtils.getToken(payload);
        return token;
    }
}
