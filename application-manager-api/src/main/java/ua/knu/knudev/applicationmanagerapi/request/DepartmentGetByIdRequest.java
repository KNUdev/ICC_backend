package ua.knu.knudev.applicationmanagerapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Builder
@Schema(name = "DepartmentGetByIdRequest", description = "Request to get a department by ID")
public record DepartmentGetByIdRequest(

        @NotNull
        @Schema(description = "Department ID",
                example = "123e4567-e89b-12d3-a456-426614174000",
                requiredMode = REQUIRED)
        UUID id
) {
}