package ua.knu.knudev.integrationtests.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "ua.knu.knudev.applicationmanager",
        "ua.knu.knudev.employeemanager",
        "ua.knu.knudev.fileservice"
})
public class IntegrationTestsConfig {
}
