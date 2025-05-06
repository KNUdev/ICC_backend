package ua.knu.knudev.employeemanagerapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
@Schema(description = "DTO representing sector data")
public record SectorDto(
        @Schema(description = "UUID format sector id", example = "1d87b3e3-44a7-4915-ac13-33180ed448ff")
        UUID id,
        @Schema(description = "Sector`s name")
        MultiLanguageFieldDto name,
        @Schema(description = "Datetime of sector creation", example = "2025-03-23T11:50:23.223")
        LocalDateTime createdAt,
        @Schema(description = "Datetime of sector update", example = "2025-03-23T11:50:23.223")
        LocalDateTime updatedAt,
        @Schema(description = "A set of sector`s specialties", implementation = ShortSpecialtyDto.class)
        Set<ShortSpecialtyDto> specialties
) {
}
