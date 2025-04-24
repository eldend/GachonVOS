package com.example.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
/**
 * LoginUserArgumentResolver가 인식될 수 있도록 WebMvcConfigurer에 추가
 */
@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        //registry.addViewController("/").setViewName("member");
//        registry.addRedirectViewController("/","/posts");
//    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("**") // Adjust as needed
                .allowedOrigins("**") // Allowed origins
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Allowed methods
                .allowedHeaders("Authorization") // Allowed headers
                .allowCredentials(true); // Allow credentials
    }

}

