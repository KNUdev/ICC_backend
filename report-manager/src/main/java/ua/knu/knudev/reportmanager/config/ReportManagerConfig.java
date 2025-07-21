package ua.knu.knudev.reportmanager.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan("ua.knu.knudev.reportmanager")
@EntityScan("ua.knu.knudev.reportmanager.domain")
@EnableScheduling
public class ReportManagerConfig {
}