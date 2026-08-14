package com.akashf.springv4.demo.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import org.springframework.core.io.ClassPathResource;

@Configuration
@ConditionalOnProperty(name = "storage.mode", havingValue = "gcp")
public class GcpConfig {

    @Value("${storage.gcp.project-id}")
    private String projectId;

    @Value("${storage.gcp.credentials}")
    private String credentialFile;

    @Bean
    Storage gcpStorage() throws IOException {

        GoogleCredentials credentials = GoogleCredentials.fromStream(
                new ClassPathResource(
                        credentialFile.replace("classpath:", ""))
                        .getInputStream());

        return StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(credentials)
                .build()
                .getService();
    }

}