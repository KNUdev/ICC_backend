package ua.knu.knudev.applicationmanagerapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ua.knu.knudev.icccommon.domain.embeddable.FullName;
import ua.knu.knudev.icccommon.enums.ApplicationStatus;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
@Schema(description = "DTO representing an application submitted by a user")
public record ApplicationDto(

        @Schema(description = "Unique identifier of the application", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,

        @Schema(description = "Full name of the applicant", example = "John Doe")
        FullName applicantName,

        @Schema(description = "Email address of the applicant", example = "john.doe@knu.ua")
        String applicantEmail,

        @Schema(description = "Timestamp when the application was received", example = "2024-06-01T15:30:00")
        LocalDateTime receivedAt,

        @Schema(description = "Timestamp when the application was completed", example = "2024-06-02T10:15:00")
        LocalDateTime completedAt,

        @Schema(description = "Description of the problem reported by the applicant", example = "Water leakage in the basement")
        String problemDescription,

        @Schema(description = "URL or path to a photo showing the problem")
        String problemPhoto,

        @Schema(description = "Current status of the application", example = "IN_PROGRESS")
        ApplicationStatus status,

        @Schema(description = "Identifier of the department handling the application." +
                "For private applications this field is null", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
        UUID departmentId,

        @Schema(description = "Set of employee IDs assigned to the application", example = "[\"b15d6f2e-5bc8-4c8f-bf4d-3c0e2a0d3e98\", \"a36d6f2e-1234-5678-bf4d-123456789abc\"]")
        Set<UUID> assignedEmployeeIds) {
}
