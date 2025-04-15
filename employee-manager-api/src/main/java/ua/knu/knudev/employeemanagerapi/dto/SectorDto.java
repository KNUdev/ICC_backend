package ua.knu.knudev.employeemanagerapi.dto;

import lombok.Builder;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
public record SectorDto(
        UUID id,
        MultiLanguageFieldDto name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Set<SpecialtyDto> specialties
) {
}
