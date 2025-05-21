package ua.knu.knudev.applicationmanagerapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import ua.knu.knudev.icccommon.enums.ApplicationStatus;

import java.util.Set;
import java.util.UUID;

@Builder
@Schema(name = "ApplicationCreateRequest", description = "Object containing data required to create a new application")
public record ApplicationCreateRequest(

        @Schema(description = "Name of the applicant",
                example = "Jane Smith",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "This field cannot be null")
        String applicantName,

        @Schema(description = "Email of the applicant",
                example = "jane.smith@example.com",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "This field cannot be null")
        String email,

        @Schema(description = "Description of the reported problem",
                example = "Air conditioning not working in office",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "This field cannot be null")
        String problemDescription,

        @Schema(description = "Photo of the reported problem (URL or file path)",
                example = "http://example.com/images/ac-issue.jpg",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "This field cannot be null")
        String problemPhoto,

        @Schema(description = "Initial status of the application",
                example = "PENDING",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "This field cannot be null")
        ApplicationStatus status,

        @Schema(description = "Department ID responsible for handling the application",
                example = "123e4567-e89b-12d3-a456-426614174000",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "This field cannot be null")
        UUID departmentId,

        @Schema(description = "IDs of employees assigned to the application",
                example = "[\"c56a4180-65aa-42ec-a945-5fd21dec0538\", \"3fa85f64-5717-4562-b3fc-2c963f66afa6\"]",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "This field cannot be null")
        Set<UUID> assignedEmployeeIds
) {
}
