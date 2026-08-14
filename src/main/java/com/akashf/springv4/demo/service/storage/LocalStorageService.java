package com.akashf.springv4.demo.service.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.akashf.springv4.demo.service.Helper;
import com.akashf.springv4.demo.service.storage.interfaces.StorageService;

@Service
@ConditionalOnProperty(name = "storage.mode", havingValue = "local", matchIfMissing = true)
public class LocalStorageService implements StorageService {

    @Value("${storage.local.path}")
    private String uploadPath;

    @Override
    public String upload(MultipartFile file) throws IOException {
        return upload(file, "./");
    }

    @Override
    public String upload(MultipartFile file, String directory) throws IOException {
        String fileName = Helper.strPluk(UUID.randomUUID().toString(), "_", file.getOriginalFilename());
        Path dir = Paths.get(uploadPath, directory);
        Files.createDirectories(dir);
        Path destination = dir.resolve(fileName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return directory + fileName;
    }

    @Override
    public byte[] download(String key) throws IOException {
        Path path = Paths.get(uploadPath, key);
        return Files.readAllBytes(path);
    }

    @Override
    public String getUrl(String key) {
        return Helper.strPluk("/uploads/", key);

    }

    @Override
    public boolean exists(String key) {
        return Files.exists(Paths.get(uploadPath, key));
    }

    @Override
    public void delete(String key) throws IOException {
        Path path = Paths.get(uploadPath, key);
        Files.delete(path);
    }

    @Override
    public void deleteIfExists(String key) throws IOException {
        Path path = Paths.get(uploadPath, key);
        Files.deleteIfExists(path);
    }

}
