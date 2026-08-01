package com.akashf.springv4.demo.filters;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.akashf.springv4.demo.service.Helper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter
 * -------
 * Executes before the request reaches Spring MVC (DispatcherServlet).
 *
 * Use a Filter for:
 * - Logging incoming HTTP requests and responses.
 * - CORS configuration.
 * - Character encoding.
 * - Request/response modification or wrapping.
 * - Compression.
 * - JWT token extraction.
 * - Global security checks that are independent of controllers.
 *
 * Scope:
 * - Works for every HTTP request.
 * - Part of the Servlet container, not Spring MVC.
 * 
 * =======================================================
 * 
 * FILTER vs INTERCEPTOR
 *
 * Filter
 * - Runs before Spring MVC.
 * - Servlet-level component.
 * - Best for logging, CORS, request wrapping, encoding, and security.
 *
 * Interceptor
 * - Runs before and after controller execution.
 * - Spring MVC component.
 * - Best for authentication, authorization, execution timing,
 * and controller-specific logic.
 */
@Component
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Helper.on("MIDDELWARE ", request.getMethod(), request.getRequestURI());
        filterChain.doFilter(request, response);
    }
}