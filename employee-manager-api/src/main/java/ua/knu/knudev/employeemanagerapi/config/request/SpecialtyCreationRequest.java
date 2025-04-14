package ua.knu.knudev.employeemanagerapi.config.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ua.knu.knudev.employeemanagerapi.config.dto.SectorDto;
import ua.knu.knudev.icccommon.constant.SpecialtyCategory;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;

import java.util.Set;

@Builder
@Schema(description = "Request object for creating a specialty")
public record SpecialtyCreationRequest(
        @NotEmpty(message = "Field 'Specialty' cannot be empty")
        @Valid
        MultiLanguageFieldDto name,

        @NotNull(message = "Field 'specialtyCategory' cannot be empty")
        @Schema(
                description = "Specialty category",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        SpecialtyCategory category,

        @NotNull(message = "Sectors set cannot be empty")
        @Schema(
                description = "A set of specialty sectors",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Set<@Valid SectorDto> sectors
) {
}
