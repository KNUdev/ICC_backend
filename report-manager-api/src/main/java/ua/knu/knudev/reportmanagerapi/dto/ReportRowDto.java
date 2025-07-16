package ua.knu.knudev.reportmanagerapi.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReportRowDto(
        Long id,
        String fullName,
        LocalDate date,
        BigDecimal points
) {}
