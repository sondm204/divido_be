package com.devido.devido_be.service;

import com.google.genai.Client;
import com.google.genai.types.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class AiService {

    private final Client client;
    private final RestTemplate restTemplate;
    public AiService() {
        this.client = new Client();
        this.restTemplate = new RestTemplate();
    }

    public String aiFormatText(String data) {
        Schema schema = Schema.builder()
            .type(Type.Known.OBJECT)
            .properties(Map.of(
                "productName", Schema.builder().type(Type.Known.STRING).build(),
                "quantity", Schema.builder().type(Type.Known.NUMBER).build(),
                "unitPrice", Schema.builder().type(Type.Known.NUMBER).build(),
                "totalPrice", Schema.builder().type(Type.Known.NUMBER).build()
            ))
            .required(List.of("productName", "quantity", "unitPrice", "totalPrice"))
            .build();

        Schema responseSchema = Schema.builder()
            .type(Type.Known.ARRAY)
            .items(schema)
            .build();

        GenerateContentConfig config = GenerateContentConfig.builder()
            .responseMimeType("application/json")
            .responseSchema(responseSchema)
            .temperature(0.1F)
            .maxOutputTokens(2048)
            .build();

        String prompt = "Extract product info as JSON array:\n" + data;

        GenerateContentResponse response = client.models.generateContent(
            "gemini-2.0-flash-exp",  // Model hỗ trợ structured output tốt hơn
            prompt,
            config  // Truyền config vào đây
        );

        return response.text();
    }

    public String extractBillFromImage(String gcsUrl) {
        StopWatch stopWatch = new StopWatch("Gemini Vision");

        try {
            stopWatch.start("Build Request & API Call");

            // Define schema
            Schema responseSchema = Schema.builder()
                .type(Type.Known.ARRAY)
                .items(Schema.builder()
                    .type(Type.Known.OBJECT)
                    .properties(Map.of(
                        "productName", Schema.builder().type(Type.Known.STRING).build(),
                        "quantity", Schema.builder().type(Type.Known.NUMBER).build(),
                        "unitPrice", Schema.builder().type(Type.Known.NUMBER).build(),
                        "totalPrice", Schema.builder().type(Type.Known.NUMBER).build()
                    ))
                    .required(List.of("productName", "quantity", "unitPrice", "totalPrice"))
                    .build())
                .build();

            GenerateContentConfig config = GenerateContentConfig.builder()
                .responseMimeType("application/json")
                .responseSchema(responseSchema)
                .temperature(0.1F)
                .maxOutputTokens(2048)
                .build();

            String prompt = """
                Trích xuất thông tin sản phẩm từ hóa đơn trong ảnh này.
                Với mỗi sản phẩm, cung cấp:
                - productName: Tên sản phẩm ngắn gọn
                - quantity: Số lượng
                - unitPrice: Đơn giá
                - totalPrice: Thành tiền
                """;

            // Sử dụng FileData cho GCS URL
            Part imagePart = Part.builder()
                .fileData(FileData.builder()
                    .mimeType(detectMimeType(gcsUrl))
                    .fileUri(convertToGcsUri(gcsUrl))  // Chuyển sang format gs://
                    .build())
                .build();

            Content content = Content.builder()
                .role("user")
                .parts(List.of(
                    Part.builder().text(prompt).build(),
                    imagePart
                ))
                .build();

            GenerateContentResponse response = client.models.generateContent(
                "gemini-2.0-flash-exp",
                List.of(content),
                config
            );

            stopWatch.stop();
            System.out.println("✅ Gemini Vision completed in {}ms" + stopWatch.getTotalTimeMillis());

            return response.text();

        } catch (Exception e) {
            System.out.println("❌ Gemini Vision failed after {}ms" +
                stopWatch.getTotalTimeMillis());
            throw new RuntimeException("Image processing failed", e);
        }
    }

    /**
     * Convert https://storage.googleapis.com/bucket/path
     * to gs://bucket/path
     */
    private String convertToGcsUri(String url) {
        if (url.startsWith("gs://")) {
            return url;
        }

        // Convert https URL to gs:// format
        if (url.startsWith("https://storage.googleapis.com/")) {
            return url.replace("https://storage.googleapis.com/", "gs://");
        }

        // Nếu là storage.cloud.google.com
        if (url.startsWith("https://storage.cloud.google.com/")) {
            return url.replace("https://storage.cloud.google.com/", "gs://");
        }

        return url;
    }

    private String detectMimeType(String url) {
        String lower = url.toLowerCase();
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".webp")) return "image/webp";
        if (lower.endsWith(".gif")) return "image/gif";
        return "image/jpeg"; // default
    }
}
