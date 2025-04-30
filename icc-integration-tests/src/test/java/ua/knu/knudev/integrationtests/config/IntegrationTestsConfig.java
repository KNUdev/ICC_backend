package ua.knu.knudev.integrationtests.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "ua.knu.knudev.applicationmanager",
        "ua.knu.knudev.employeemanager",
        "ua.knu.knudev.fileservice"
})

@EnableJpaRepositories(basePackages = {"ua.knu.knudev.integrationtests"})
public class IntegrationTestsConfig {
}
