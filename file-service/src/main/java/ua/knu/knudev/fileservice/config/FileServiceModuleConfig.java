package ua.knu.knudev.fileservice.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("ua.knu.knudev.fileservice")
@EnableConfigurationProperties({ImageFileConfigProperties.class, TaskFileConfigProperties.class})
public class FileServiceModuleConfig {

}
