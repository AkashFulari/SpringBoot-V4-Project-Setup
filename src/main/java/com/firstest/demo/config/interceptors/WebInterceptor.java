package com.firstest.demo.config.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.firstest.demo.service.Helper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interceptor
 * -----------
 * Executes before and after a controller method is invoked.
 *
 * Use an Interceptor for:
 * - Authentication and authorization.
 * - Controller execution time logging.
 * - Access control based on controller or endpoint.
 * - Adding common request attributes.
 * - Executing logic before or after controller methods.
 *
 * Scope:
 * - Works only with Spring MVC requests.
 * - Has access to the selected controller (handler).
 * - Cannot modify the request as extensively as a Filter.
 */
@Component
public class WebInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Helper.on("Before Controller");
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        Helper.on("After Controller");
    }
}