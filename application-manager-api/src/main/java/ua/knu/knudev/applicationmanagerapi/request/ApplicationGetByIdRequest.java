package ua.knu.knudev.applicationmanagerapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.UUID;

@Builder
@Schema(name = "ApplicationGetByIdRequest", description = "Request to get an application by its ID")
public record ApplicationGetByIdRequest(
        @Schema(description = "Application ID", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "ID cannot be null")
        UUID id
) {}
