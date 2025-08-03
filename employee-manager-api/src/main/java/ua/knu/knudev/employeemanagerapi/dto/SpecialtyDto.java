package ua.knu.knudev.employeemanagerapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import ua.knu.knudev.icccommon.constant.SpecialtyCategory;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
@Schema(description = "DTO representing specialty data")
public record SpecialtyDto(
        @Schema(description = "UUID format specialty id", example = "1d87b3e3-44a7-4915-ac13-33180ed448ff")
         UUID id,
        @Schema(description = "Specialty name", implementation = MultiLanguageFieldDto.class)
         MultiLanguageFieldDto name,
        @Schema(description = "Datetime of specialty creation", example = "2025-03-23T11:50:23.223")
         LocalDateTime createdAt,
        @Schema(description = "Datetime of specialty update", example = "2025-03-23T11:50:23.223")
         LocalDateTime updatedAt,
        @Schema(description = "A category of specialty", implementation = SpecialtyCategory.class)
         SpecialtyCategory category,
        @Schema(description = "A set of specialty sectors", implementation = ShortSectorDto.class)
         Set<ShortSectorDto> sectors,
        @Schema(description = "Field determines whether the object will be publicly visible", example = "true")
        Boolean isPublic
) {
}
