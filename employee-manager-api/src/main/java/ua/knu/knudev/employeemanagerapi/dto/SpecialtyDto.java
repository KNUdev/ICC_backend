package ua.knu.knudev.employeemanagerapi.dto;

import lombok.Builder;
import ua.knu.knudev.icccommon.constant.SpecialtyCategory;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
public record SpecialtyDto(
         UUID id,
         MultiLanguageFieldDto name,
         LocalDateTime createdAt,
         LocalDateTime updatedAt,
         SpecialtyCategory category,
         Set<ShortSectorDto> sectors
) {
}
