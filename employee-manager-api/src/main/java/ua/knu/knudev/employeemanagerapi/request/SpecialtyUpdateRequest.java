package ua.knu.knudev.employeemanagerapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ua.knu.knudev.employeemanagerapi.dto.SectorDto;
import ua.knu.knudev.icccommon.constant.SpecialtyCategory;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;

import java.util.Set;
import java.util.UUID;

@Builder
@Schema(description = "Request object for update a specialty")
public record SpecialtyUpdateRequest(
        @NotNull(message = "ID cannot be empty")
        @Schema(
                description = "Specialty ID",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        UUID id,

        @Schema(
                description = "Specialty name",
                requiredMode = Schema.RequiredMode.REQUIRED,
                implementation = MultiLanguageFieldDto.class
        )
        MultiLanguageFieldDto name,

        @Schema(
                description = "Specialty category",
                requiredMode = Schema.RequiredMode.REQUIRED,
                implementation = SpecialtyCategory.class
        )
        SpecialtyCategory category,

        @Schema(
                description = "A set of specialty sectors",
                requiredMode = Schema.RequiredMode.REQUIRED,
                implementation = SectorDto.class
        )
        Set<@Valid SectorDto> sectors
) {
}
