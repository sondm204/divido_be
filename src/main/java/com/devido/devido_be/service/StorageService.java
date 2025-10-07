package com.devido.devido_be.service;

import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class StorageService {
    private final Storage storage;

    @Value("${gcp.bucket-name}")
    private String bucketName;

    public StorageService(Storage storage) {
        this.storage = storage;
    }


    public String uploadImage(MultipartFile file) throws IOException {
        String objectName = file.getOriginalFilename();

        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
            .setContentType(file.getContentType())
            .build();

        storage.create(blobInfo, file.getBytes());

        // URL public của file
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, objectName);
    }

    public boolean deleteFile(String fileName) {
        try {
            boolean deleted = storage.delete(bucketName, fileName);
            if (deleted) {
                System.out.println("Deleted file: " + fileName);
            } else {
                System.out.println("File not found: " + fileName);
            }
            return deleted;
        } catch (StorageException e) {
            System.err.println("Error deleting file: " + e.getMessage());
            return false;
        }
    }

}
