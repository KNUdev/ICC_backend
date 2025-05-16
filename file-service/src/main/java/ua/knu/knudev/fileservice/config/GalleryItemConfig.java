package ua.knu.knudev.fileservice.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("ua.knu.knudev.fileservice")
@EnableJpaRepositories(basePackages = "ua.knu.knudev.fileservice.repository")
@EntityScan("ua.knu.knudev.fileservice.domain")
public class GalleryItemConfig {
}