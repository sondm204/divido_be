package com.devido.devido_be.config;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GcpConfig {

    @Value("${gcp.project-id}")
    private String projectId;

    @Bean
    public Storage storage() throws IOException {
        // Tự động sử dụng Application Default Credentials (ADC)
        Credentials credentials = GoogleCredentials.getApplicationDefault();
        return StorageOptions.newBuilder()
            .setProjectId(projectId)
            .setCredentials(credentials)
            .build()
            .getService();
    }
}
