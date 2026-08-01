package com.firstest.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.firstest.demo.config.interceptors.WebInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final WebInterceptor interceptor;

    public WebConfig(WebInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);
    }
}