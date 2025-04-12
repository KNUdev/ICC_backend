package ua.knu.knudev.fileservice.adapter.minio;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MinioConfig {

    private final MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() {
        String clientEndpoint = minioProperties.isUseProxy() ? minioProperties.getInternalUrl()
                : minioProperties.getExternalUrl();
        return MinioClient.builder()
                .endpoint(clientEndpoint)
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }
}
