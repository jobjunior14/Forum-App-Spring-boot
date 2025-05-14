package com.example.forum.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
// import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {
    @Value("${file.upload-dir}")
    private String uploadDir;

    public String storeFile(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get(System.getProperty("user.dir"), uploadDir);
            Files.createDirectories(uploadPath);
            Path filePath = uploadPath.resolve(fileName);
            file.transferTo(filePath.toFile());
            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
}
