package com.akashf.springv4.demo.service.storage;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.akashf.springv4.demo.service.Helper;
import com.akashf.springv4.demo.service.storage.interfaces.StorageService;

@Service
public class StorageFactory {

    private final String mode;

    private final ObjectProvider<AwsS3StorageService> awsProvider;
    private final ObjectProvider<AzureStorageService> azureProvider;
    private final ObjectProvider<GcpStorageService> gcpProvider;
    private final ObjectProvider<LocalStorageService> localProvider;

    public StorageFactory(
            @Value("${storage.mode:local}") String mode,
            ObjectProvider<AwsS3StorageService> awsProvider,
            ObjectProvider<AzureStorageService> azureProvider,
            ObjectProvider<GcpStorageService> gcpProvider,
            ObjectProvider<LocalStorageService> localProvider) {
        Helper.o("StorageFactory initialized with mode: ", mode);
        this.mode = mode;
        this.awsProvider = awsProvider;
        this.azureProvider = azureProvider;
        this.gcpProvider = gcpProvider;
        this.localProvider = localProvider;
    }

    public StorageService getStorage() {
        return switch (mode.toLowerCase()) {
            case "s3" -> awsProvider.getObject();
            case "azure" -> azureProvider.getObject();
            case "gcp" -> gcpProvider.getObject();
            case "local" -> localProvider.getObject();
            default -> throw new IllegalArgumentException(
                    "Unsupported storage.mode: " + mode);
        };
    }
}