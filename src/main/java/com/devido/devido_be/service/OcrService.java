package com.devido.devido_be.service;

import com.mindee.input.URLInputSource;
import com.mindee.parsing.v2.field.DynamicField;
import com.mindee.parsing.v2.field.ObjectField;
import com.mindee.parsing.v2.field.SimpleField;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.mindee.MindeeClientV2;
import com.mindee.InferenceParameters;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.v2.InferenceResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class OcrService {
    private final MindeeClientV2 mindeeClient;
    private final String modelId;

    public OcrService(@Value("${mindee.api-key}") String apiKey, @Value("${mindee.model-id}") String modelId) {
        this.modelId = modelId;
        this.mindeeClient = new MindeeClientV2(apiKey);
    }

    public String imageToText(String url) {
        try {

            InferenceParameters options = InferenceParameters
                .builder(modelId)
                .rag(null)
                .rawText(null)
                .polygon(null)
                .confidence(null)
                .build();

            URLInputSource inputSource = URLInputSource
                .builder(url)
                .build();

            InferenceResponse response = mindeeClient.enqueueAndGetInference(
                inputSource,
                options
            );

            return response.getInference().getResult().getFields().getListField("line_items").toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
