package ua.knu.knudev.employeemanagerapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ua.knu.knudev.icccommon.constant.SpecialtyCategory;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;

import java.util.Set;
import java.util.UUID;

@Builder
@Schema(description = "Request object for creating a specialty")
public record SpecialtyCreationRequest(
        @Valid
        @Schema(
                description = "Specialty name",
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
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Set<UUID> sectorsIds
) {
}