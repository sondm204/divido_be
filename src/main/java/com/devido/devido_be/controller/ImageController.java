package com.devido.devido_be.controller;

import com.devido.devido_be.dto.ApiResponse;
import com.devido.devido_be.service.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private StorageService storageService;

    public ImageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = storageService.uploadImage(file);
            return ResponseEntity.ok(new ApiResponse<>(true, "Image uploaded successfully", imageUrl));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteImage(@RequestParam("fileName") String fileName) {
        boolean deleted = storageService.deleteFile(fileName);
        if (deleted) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Image deleted successfully", null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Image not found", null));}
    }
}