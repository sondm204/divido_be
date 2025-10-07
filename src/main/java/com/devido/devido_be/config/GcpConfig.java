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

    @Value("${gcp.credentials.location:}")
    private String credentialsPath;

    @Bean
    public Storage storage() throws IOException {
        StorageOptions.Builder optionsBuilder = StorageOptions.newBuilder().setProjectId(projectId);

        // Nếu có khai báo credentialsPath → chạy local (hoặc VPS ngoài GCP)
        if (credentialsPath != null && !credentialsPath.isBlank()) {
            InputStream credentialsStream;
            if (credentialsPath.startsWith("classpath:")) {
                String resourcePath = credentialsPath.replace("classpath:", "");
                credentialsStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
            } else if (credentialsPath.startsWith("file:")) {
                String filePath = credentialsPath.replace("file:", "");
                credentialsStream = new FileInputStream(filePath);
            } else {
                throw new IllegalArgumentException("Unsupported credentials path format: " + credentialsPath);
            }

            Credentials credentials = ServiceAccountCredentials.fromStream(credentialsStream);
            optionsBuilder.setCredentials(credentials);

            System.out.println("🔑 Using explicit credentials from: " + credentialsPath);
        } else {
            // Nếu không có credentialsPath → chạy trong Cloud Run (tự động nhận)
            Credentials defaultCredentials = GoogleCredentials.getApplicationDefault();
            optionsBuilder.setCredentials(defaultCredentials);
            System.out.println("🌩️ Using default GCP credentials (Cloud Run / GCE / GKE)");
        }

        return optionsBuilder.build().getService();
    }
}
