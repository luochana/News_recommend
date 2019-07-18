package com.luochen.web.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyMvcConf implements WebMvcConfigurer {
    @Bean
    public WebMvcConfigurer webMvcConfigurer()
    {
        WebMvcConfigurer conf=new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {

            }

            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/success.html").setViewName("pages/login");
                registry.addViewController("/register.html").setViewName("pages/register");
                registry.addViewController("/login.html").setViewName("pages/login");
                registry.addViewController("/index.html").setViewName("pages/index");
                registry.addViewController("/").setViewName("pages/index");

            }
        };
        return conf;
    }
}
