package com.firstest.demo.service.storage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobClient;
import com.firstest.demo.service.Helper;
import com.firstest.demo.service.storage.interfaces.StorageService;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class AzureStorageService implements StorageService {
    private final BlobContainerClient container;

    public AzureStorageService(
            BlobContainerClient container) {

        this.container = container;

    }

    @Override
    public String upload(MultipartFile file) throws IOException {
        return upload(file, "./");
    }

    @Override
    public String upload(MultipartFile file, String directory) throws IOException {
        String key = Helper.strPluk(directory, UUID.randomUUID().toString(), "_", file.getOriginalFilename());
        BlobClient blob = container.getBlobClient(key);
        blob.upload(new ByteArrayInputStream(file.getBytes()), file.getSize(), true);
        return key;
    }

    @Override
    public byte[] download(String key) throws IOException {
        BlobClient blob = container.getBlobClient(key);
        return blob.downloadContent().toBytes();
    }

    @Override
    public String getUrl(String key) {
        return container.getBlobClient(key).getBlobUrl();
    }

    @Override
    public boolean exists(String key) {
        return container.getBlobClient(key).exists();
    }

    @Override
    public void delete(String key) throws IOException {
        container.getBlobClient(key).delete();
    }

    @Override
    public void deleteIfExists(String key) throws IOException {
        if (exists(key))
            delete(key);
    }
}
