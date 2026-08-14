package com.akashf.springv4.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;

@Configuration
@ConditionalOnProperty(name = "storage.mode", havingValue = "azure")
public class AzureConfig {

    @Value("${storage.azure.connection-string}")
    private String connectionString;

    @Value("${storage.azure.container}")
    private String container;

    @Bean
    BlobContainerClient blobContainerClient() {
        BlobContainerClient client = new BlobContainerClientBuilder()
                .connectionString(connectionString)
                .containerName(container)
                .buildClient();

        if (!client.exists()) {
            client.create();
        }

        return client;
    }

}