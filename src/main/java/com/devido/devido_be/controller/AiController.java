package com.devido.devido_be.controller;

import com.devido.devido_be.dto.BillScanResponse;
import com.devido.devido_be.service.AiService;
import com.devido.devido_be.service.OcrService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("/ai")
public class AiController {
    private final OcrService ocrService;
    private final AiService aiService;
    private final ObjectMapper objectMapper;

    public AiController(OcrService ocrService, AiService aiService, ObjectMapper objectMapper) {
        this.ocrService = ocrService;
        this.aiService = aiService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/handle-bill")
    public ResponseEntity<?> ocr(@RequestParam String url) throws ExecutionException, InterruptedException {

//        long ocrStart = System.currentTimeMillis();
//        String data = imageToTextAsync(url).get();
//        long ocrTime = System.currentTimeMillis() - ocrStart;
//        System.out.println("⏱️ OCR Service took: " + ocrTime);

        long aiStart = System.currentTimeMillis();
        String billsText = aiService.extractBillFromImage(url);
//        String billsText = aiFormatTextAsync(data).get();
        long aiTime = System.currentTimeMillis() - aiStart;
        System.out.println("⏱️AI Service took: " + aiTime);

        try {
            BillScanResponse[] bills = objectMapper.readValue(billsText, BillScanResponse[].class);
            return ResponseEntity.ok(bills);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Invalid AI output format.");
        }
    }

    @Async
    public CompletableFuture<String> imageToTextAsync(String url) {
        return CompletableFuture.supplyAsync(() -> ocrService.imageToText(url));
    }

    @Async
    public CompletableFuture<String> aiFormatTextAsync(String text) {
        return CompletableFuture.supplyAsync(() -> aiService.aiFormatText(text));
    }
}
