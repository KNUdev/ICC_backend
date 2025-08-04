package ua.knu.knudev.reportmanagerapi.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReportRowDto(
        Long id,
        String nameSurname,
        String phoneNumber,
        String email,
        String position,
        BigDecimal salary,
        LocalDate contractValidTo
) {}
