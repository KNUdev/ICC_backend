package ua.knu.knudev.applicationmanagerapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ua.knu.knudev.applicationmanagerapi.dto.MultiLanguageFieldDto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Builder
@Schema(name = "DepartmentCreateRequest", description = "Data required to create a new department")
public record DepartmentCreateRequest(

        @Valid
        @NotNull
        @Schema(description = "Multi-language department name",
                implementation = MultiLanguageFieldDto.class,
                requiredMode = REQUIRED)
        MultiLanguageFieldDto name
) {
}