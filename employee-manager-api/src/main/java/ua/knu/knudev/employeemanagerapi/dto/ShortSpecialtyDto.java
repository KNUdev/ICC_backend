package ua.knu.knudev.employeemanagerapi.dto;

import ua.knu.knudev.icccommon.constant.SpecialtyCategory;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ShortSpecialtyDto(
        UUID id,
        MultiLanguageFieldDto name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        SpecialtyCategory category
) {
}