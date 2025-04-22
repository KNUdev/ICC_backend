package ua.knu.knudev.employeemanagerapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ua.knu.knudev.employeemanagerapi.dto.SpecialtyDto;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;

import java.util.Set;
import java.util.UUID;

@Builder
@Schema(description = "Request object for updating a sector")
public record SectorUpdateRequest(
        @NotNull
        @Schema(
                description = "Sector id",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        UUID id,

        @Schema(
                description = "Sector name",
                requiredMode = Schema.RequiredMode.REQUIRED,
                implementation = MultiLanguageFieldDto.class
        )
        MultiLanguageFieldDto name,

        @Schema(
                description = "A set of sector specialties",
                requiredMode = Schema.RequiredMode.REQUIRED,
                implementation = SpecialtyDto.class
        )
        Set<@Valid SpecialtyDto> specialties
) {
}
