package com.firstest.demo.service.storage;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.firstest.demo.service.Helper;
import com.firstest.demo.service.storage.interfaces.StorageService;

import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

@Service
public class AwsS3StorageService implements StorageService {
    @Value("${aws.access-key}")
    private String accessKey;

    @Value("${aws.secret-key}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.bucket-name}")
    private String bucketName;

    private final S3Client s3;

    public AwsS3StorageService(S3Client s3) {
        this.s3 = s3;
    }

    @Override
    public String upload(MultipartFile file) throws IOException {
        String key = Helper.strPluk("./", UUID.randomUUID().toString(), "_", file.getOriginalFilename());
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();
        s3.putObject(request, RequestBody.fromBytes(file.getBytes()));
        return key;
    }

    @Override
    public String upload(MultipartFile file, String directory) throws IOException {
        String key = Helper.strPluk("./", directory, UUID.randomUUID().toString(), "_", file.getOriginalFilename());
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();
        s3.putObject(request, RequestBody.fromBytes(file.getBytes()));
        return key;
    }

    @Override
    public byte[] download(String key) throws IOException {
        GetObjectRequest request = GetObjectRequest.builder().bucket(bucketName).key(key).build();
        ResponseBytes<GetObjectResponse> object = s3.getObjectAsBytes(request);
        return object.asByteArray();

    }

    @Override
    public String getUrl(String key) {
        return Helper.strPluk("https://", bucketName, ".s3.", region, ".amazonaws.com/", key);
    }

    @Override
    public boolean exists(String key) {
        try {
            s3.headObject(HeadObjectRequest.builder().bucket(bucketName).key(key).build());
            return true;
        } catch (NoSuchKeyException ex) {
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public void delete(String key) {
        s3.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(key).build());
    }

    @Override
    public void deleteIfExists(String key) throws IOException {
        if (exists(key))
            delete(key);
    }
}
