package com.akashf.springv4.demo.config;

import com.akashf.springv4.demo.service.Helper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import jakarta.annotation.PostConstruct;
import java.io.IOException;

@Configuration
@ConditionalOnProperty(name = "fcm.notify", havingValue = "true")
public class FCMConfig {
    @PostConstruct
    public void initialize() {
        try {
            Helper.o(">>> Initializing Firebase...");
            FirebaseOptions options = FirebaseOptions.builder().setCredentials(GoogleCredentials
                    .fromStream(new ClassPathResource("firebase-service-account.json").getInputStream()))
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                Helper.o(">>> Firebase initialized successfully");
            } else {
                Helper.o(">>> Firebase already initialized");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize Firebase Admin SDK", e);
        }
    }
}
