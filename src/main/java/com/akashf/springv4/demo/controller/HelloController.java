package com.akashf.springv4.demo.controller;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    private final MessageSource messageSource;

    public HelloController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Returns localized message.
     *
     * Locale is automatically created by Spring
     * from the Accept-Language request header.
     *
     * Example:
     *
     * Header:
     * Accept-Language: en
     *
     * Response:
     * Welcome John
     *
     *
     * Header:
     * Accept-Language: hi
     *
     * Response:
     * स्वागत है John
     */
    @GetMapping("/hello")
    public String sayHello(Locale locale) {
        return messageSource.getMessage("welcome", new Object[] { "Akash Fulari" }, locale);
    }
}