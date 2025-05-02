package ua.knu.knudev.employeemanagerapi.dto;

import lombok.Builder;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ShortSectorDto(
        UUID id,
        MultiLanguageFieldDto name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
