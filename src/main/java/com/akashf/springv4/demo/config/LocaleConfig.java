package com.akashf.springv4.demo.config;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.LocaleResolver;

/**
 * ==========================================================
 * Spring Boot Localization (i18n) Configuration Example
 * ==========================================================
 *
 * Purpose:
 * --------
 * Allows API responses/messages to be returned in different
 * languages based on the client's locale.
 *
 * Example languages:
 * - English -> lang_en.properties
 * - Hindi -> lang_hi.properties
 * - French -> lang_fr.properties
 *
 *
 * Project Structure:
 * ------------------
 *
 * src/main/resources/
 *
 * i18n/
 * lang.properties (Default language)
 * lang_en.properties (English)
 * lang_hi.properties (Hindi)
 *
 *
 * Example file:
 *
 * lang_en.properties
 * ----------------------
 * welcome=Welcome {0}
 *
 *
 * lang_hi.properties
 * ----------------------
 * welcome=स्वागत है {0}
 *
 *
 * ==========================================================
 * MessageSource Configuration
 * ==========================================================
 *
 * MessageSource loads the messages from the i18n folder.
 *
 * classpath:locales/lang
 *
 * Spring automatically selects the file based on Locale.
 *
 * Example request:
 *
 * Header:
 * Accept-Language: hi
 *
 * Spring loads:
 *
 * lang_hi.properties
 *
 */
@Configuration
public class LocaleConfig {
    /**
     * MessageSource is responsible for loading localized messages.
     *
     * It reads messages from files like:
     *
     * locales/lang.properties -> Default language
     * locales/lang_en.properties -> English
     * locales/lang_hi.properties -> Hindi
     * locales/lang_fr.properties -> French
     *
     * Spring automatically selects the correct file based on Locale.
     *
     * Example:
     *
     * Request Header:
     * Accept-Language: hi
     *
     * Spring will load:
     * locales/lang_hi.properties
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasename("classpath:locales/lang");
        source.setDefaultEncoding("UTF-8");
        return source;
    }

    /**
     * LocaleResolver decides which language to use.
     *
     * This implementation reads the HTTP header:
     *
     * Accept-Language: en
     * Accept-Language: hi
     *
     * Example:
     *
     * GET /api/message
     *
     * Header:
     * Accept-Language: hi
     *
     * Response:
     * स्वागत है John
     *
     */
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        // Used when client does not send language header
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    }
}