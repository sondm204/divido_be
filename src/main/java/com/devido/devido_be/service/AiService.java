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
}
