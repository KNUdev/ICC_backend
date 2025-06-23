package ua.knu.knudev.integrationtests.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "ua.knu.knudev.applicationmanager",
        "ua.knu.knudev.employeemanager",
        "ua.knu.knudev.fileservice",
        "ua.knu.knudev.icccommon",
        "ua.knu.knudev.iccsecurity"
})
public class IntegrationTestsConfig {
}
