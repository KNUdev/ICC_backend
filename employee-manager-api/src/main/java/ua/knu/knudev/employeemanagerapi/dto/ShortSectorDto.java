package ua.knu.knudev.employeemanagerapi.dto;

import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ShortSectorDto(
        UUID id,
        MultiLanguageFieldDto name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Boolean isPublic
) {
}