package ua.knu.knudev.employeemanagerapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Builder;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;

import java.util.Set;
import java.util.UUID;

@Builder
@Schema(description = "Request object for creating a sector")
public record SectorCreationRequest(
        @Valid
        @Schema(
                description = "Sector name",
                requiredMode = Schema.RequiredMode.REQUIRED,
                implementation = MultiLanguageFieldDto.class
        )
        MultiLanguageFieldDto name,

        @Schema(
                description = "A set of specialties' ids",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Set<UUID> specialtiesIds
) {
}