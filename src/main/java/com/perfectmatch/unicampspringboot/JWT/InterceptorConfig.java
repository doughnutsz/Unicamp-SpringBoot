package com.perfectmatch.unicampspringboot.JWT;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .allowedMethods("*")
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
              registry.addInterceptor(new JWTInterceptors())// 登陆拦截
                .addPathPatterns("/**")
//                .excludePathPatterns(
//                        "/error",
//                        "/api/admin/login",
//                        "/api/login")
        ;
//                     registry.addInterceptor(new AdminInterceptors())// Admin拦截
//                .addPathPatterns("/api/admin/**")
//                .excludePathPatterns(
//                        "/api/admin/login")
        ;
    }

}
