package ua.knu.knudev.fileservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.util.unit.DataUnit;

import java.util.Set;

@ConfigurationProperties(prefix = "application.files.pdfs.tasks")
public record TaskFileConfigProperties(Set<String> allowedExtensions,
                                       @DataSizeUnit(DataUnit.KILOBYTES) Integer maximumSizeInKilobytes) {
}