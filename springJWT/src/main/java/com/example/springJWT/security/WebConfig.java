package com.example.springJWT.security;

import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ModelAndView;

@Configuration
public class WebConfig {

    @Bean
    public ErrorViewResolver errorViewResolver(ApplicationContext context){
        return (request, status, model) -> {
            String path="";
            switch(status.value()) {
                case 403: case 404: case 500:
                    path=String.valueOf(status.value());
                    break;
                default:
                    path="error";
                    break;
            }
            return new ModelAndView(path);
        };
    }
}