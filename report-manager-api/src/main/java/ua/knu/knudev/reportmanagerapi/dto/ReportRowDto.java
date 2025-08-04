package ua.knu.knudev.reportmanagerapi.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record ReportRowDto(
        UUID id,
        String name,
        String phoneNumber,
        String email,
        String position,
        Double salary,
        LocalDate contractValidTo
) {
}
