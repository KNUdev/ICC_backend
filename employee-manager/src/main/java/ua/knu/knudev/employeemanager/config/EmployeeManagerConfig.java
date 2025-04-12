package ua.knu.knudev.employeemanager.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("ua.knu.knudev.employeemanager")
@EnableJpaRepositories(basePackages = "ua.knu.knudev.employeemanager.repository")
@EntityScan("ua.knu.knudev.employeemanager.domain")
public class EmployeeManagerConfig {
}
