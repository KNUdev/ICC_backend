package ua.knu.knudev.applicationmanagerapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;

import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Builder
@Schema(name = "DepartmentUpdateRequest", description = "Data required to update an existing department")
public record DepartmentUpdateRequest(

        @NotNull
        @Schema(description = "Department ID",
                example = "123e4567-e89b-12d3-a456-426614174000",
                requiredMode = REQUIRED)
        UUID id,

        @Valid
        @Schema(description = "Updated multi-language department name",
                implementation = MultiLanguageFieldDto.class)
        MultiLanguageFieldDto name
) {
}
