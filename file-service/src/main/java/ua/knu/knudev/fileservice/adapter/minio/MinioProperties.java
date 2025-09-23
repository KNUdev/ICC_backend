package ua.knu.knudev.fileservice.adapter.minio;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "application.minio")
public class MinioProperties {
    private String internalUrl;
    private String externalUrl;
    @Value("${application.base-url}")
    private String filePathUrl;
    private String accessKey;
    private String secretKey;
    private boolean useProxy;
}
