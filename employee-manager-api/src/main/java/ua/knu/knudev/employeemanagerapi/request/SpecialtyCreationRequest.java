package ua.knu.knudev.employeemanagerapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ua.knu.knudev.employeemanagerapi.dto.SectorDto;
import ua.knu.knudev.icccommon.constant.SpecialtyCategory;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;

import java.util.Set;

@Builder
@Schema(description = "Request object for creating a specialty")
public record SpecialtyCreationRequest(
        @NotEmpty(message = "Field 'Specialty' cannot be empty")
        @Valid
        @Schema(
                description = "Specialty ID",
                requiredMode = Schema.RequiredMode.REQUIRED,
                implementation = MultiLanguageFieldDto.class
        )
        MultiLanguageFieldDto name,

        @NotNull(message = "Field 'specialtyCategory' cannot be empty")
        @Schema(
                description = "Specialty category",
                requiredMode = Schema.RequiredMode.REQUIRED,
                implementation = SpecialtyCategory.class
        )
        SpecialtyCategory category,

        @NotNull(message = "Sectors set cannot be empty")
        @Schema(
                description = "A set of specialty sectors",
                requiredMode = Schema.RequiredMode.REQUIRED,
                implementation = SectorDto.class
        )
        Set<@Valid SectorDto> sectors
) {
}
