package ua.knu.knudev.applicationmanager.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("ua.knu.knudev.applicationmanager")
@EnableJpaRepositories(basePackages = "ua.knu.knudev.applicationmanager.repository")
@EntityScan("ua.knu.knudev.applicationmanager.domain")
public class ApplicationManagerConfig {

}
