package ua.knu.knudev.iccapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ua.knu.knudev.applicationmanager.config.ApplicationManagerConfig;
import ua.knu.knudev.applicationmanagerapi.config.ApplicationManagerApiConfig;
import ua.knu.knudev.employeemanager.config.EmployeeManagerConfig;
import ua.knu.knudev.employeemanagerapi.config.EmployeeManagerApiConfig;
import ua.knu.knudev.fileservice.config.FileServiceModuleConfig;
import ua.knu.knudev.fileserviceapi.config.FileServiceAPIModuleConfig;
import ua.knu.knudev.icccommon.config.CommonConfig;
import ua.knu.knudev.iccliquibase.LiquibaseConfig;
import ua.knu.knudev.iccrest.config.RestModuleConfig;
import ua.knu.knudev.iccsecurity.config.SecurityModuleConfig;
import ua.knu.knudev.iccsecurityapi.config.SecurityApiConfig;
import ua.knu.knudev.reportmanager.config.ReportManagerConfig;
import ua.knu.knudev.reportmanagerapi.config.ReportManagerApiConfig;

@SpringBootApplication
@Import({ApplicationManagerConfig.class, ApplicationManagerApiConfig.class, EmployeeManagerConfig.class,
EmployeeManagerApiConfig.class, FileServiceModuleConfig.class, FileServiceAPIModuleConfig.class, CommonConfig.class,
LiquibaseConfig.class, RestModuleConfig.class, SecurityModuleConfig.class, SecurityApiConfig.class, ReportManagerConfig.class,
ReportManagerApiConfig.class})
public class IccApplication {
    public static void main(String[] args) {
        SpringApplication.run(IccApplication.class, args);
    }
}