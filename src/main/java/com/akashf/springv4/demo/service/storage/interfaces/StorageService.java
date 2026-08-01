package com.akashf.springv4.demo.service.storage.interfaces;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String upload(MultipartFile file) throws IOException;

    String upload(MultipartFile file, String directory) throws IOException;

    byte[] download(String key) throws IOException;

    String getUrl(String key);

    boolean exists(String key);

    void delete(String key) throws IOException;

    void deleteIfExists(String key) throws IOException;
}