package ua.knu.knudev.applicationmanagerapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.UUID;

@Builder
@Schema(name = "ApplicationDeleteRequest", description = "Request to delete an application")
public record ApplicationDeleteRequest(
        @Schema(description = "ID of the application to be deleted", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "ID cannot be null")
        UUID id
) {}
