package ua.knu.knudev.reportmanager.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan("ua.knu.knudev.reportmanager")
@EntityScan("ua.knu.knudev.reportmanager.domain")
@EnableJpaRepositories("ua.knu.knudev.reportmanager.repository")
@EnableScheduling
public class ReportManagerConfig {
}
