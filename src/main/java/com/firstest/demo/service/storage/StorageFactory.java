package com.firstest.demo.service.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.firstest.demo.service.storage.interfaces.StorageService;

@Service
public class StorageFactory {

    @Value("${storage.mode}")
    private String mode;

    private final AwsS3StorageService aws;
    private final AzureStorageService azure;
    private final GcpStorageService gcp;
    private final LocalStorageService local;

    public StorageFactory(AwsS3StorageService aws, AzureStorageService azure, GcpStorageService gcp,
            LocalStorageService local) {
        this.aws = aws;
        this.azure = azure;
        this.gcp = gcp;
        this.local = local;
    }

    public StorageService getStorage() {
        return switch (mode.toLowerCase()) {
            case "s3" -> aws;
            case "azure" -> azure;
            case "gcp" -> gcp;
            default -> local;
        };
    }

}