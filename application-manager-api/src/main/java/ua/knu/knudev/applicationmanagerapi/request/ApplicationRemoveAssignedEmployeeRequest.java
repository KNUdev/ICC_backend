package ua.knu.knudev.applicationmanagerapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.UUID;

@Builder
@Schema(name = "ApplicationRemoveAssignedEmployeeRequest", description = "Request to remove an assigned employee")
public record ApplicationRemoveAssignedEmployeeRequest(
        @Schema(description = "Application ID", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Application ID cannot be null")
        UUID applicationId,

        @Schema(description = "Employee ID to remove", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Employee ID cannot be null")
        UUID employeeId
) {}
