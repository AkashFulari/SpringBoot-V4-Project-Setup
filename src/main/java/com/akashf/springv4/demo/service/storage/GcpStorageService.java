package com.akashf.springv4.demo.service.storage;

import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.akashf.springv4.demo.service.Helper;
import com.akashf.springv4.demo.service.storage.interfaces.StorageService;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

@Service
public class GcpStorageService implements StorageService {
    private final Storage storage;

    @Value("${storage.gcp.bucket}")
    private String bucket;

    public GcpStorageService(Storage storage) {
        this.storage = storage;
    }

    @Override
    public String upload(MultipartFile file) throws IOException {
        return upload(file, "./");
    }

    @Override
    public String upload(MultipartFile file, String directory) throws IOException {
        String key = Helper.strPluk(directory, UUID.randomUUID().toString(), "_", file.getOriginalFilename());
        BlobId blobId = BlobId.of(bucket, key);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
        storage.create(blobInfo, file.getBytes());
        return key;
    }

    @Override
    public byte[] download(String key) throws IOException {
        Blob blob = storage.get(bucket, key);
        if (!Helper.isNN(blob))
            throw new IOException("File not found.");

        return blob.getContent();
    }

    @Override
    public String getUrl(String key) {
        return Helper.strPluk("https://storage.googleapis.com/", bucket, "/", key);
    }

    @Override
    public boolean exists(String key) {
        Blob blob = storage.get(bucket, key);
        return blob != null;
    }

    @Override
    public void delete(String key) throws IOException {
        storage.delete(bucket, key);
    }

    @Override
    public void deleteIfExists(String key) throws IOException {
        if (exists(key))
            delete(key);
    }
}
