package ua.knu.knudev.applicationmanagerapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;
import ua.knu.knudev.icccommon.domain.embeddable.FullName;
import ua.knu.knudev.icccommon.dto.FullNameDto;
import ua.knu.knudev.icccommon.enums.ApplicationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Schema(name = "ApplicationUpdateRequest", description = "Object containing data for updating an application")
public record ApplicationUpdateRequest(

        @Schema(description = "Updatable application ID",
                example = "550e8400-e29b-41d4-a716-446655440000",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "ID cannot be null")
        UUID id,

        @Schema(description = "Name of the applicant",
                example = "John Doe")
        FullNameDto applicantName,

        @Schema(description = "Email of the applicant",
                example = "john.doe@knu.ua")
        String applicantEmail,

        @Schema(description = "Date and time when the application was completed",
                example = "2024-05-02T17:45:00")
        LocalDateTime completedAt,

        @Schema(description = "Description of the problem",
                example = "Internet connectivity issue in room 302")
        String problemDescription,

        @Schema(description = "Photo of the reported problem (URL or file path)",
                example = "http://example.com/images/ac-issue.jpg",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "This field cannot be null")
        String problemPhotoName,

        @Schema(description = "Photo which was uploaded to describe the problem",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "This field cannot be null")
        MultipartFile problemPhoto,

        @Schema(description = "Current status of the application",
                example = "IN_PROGRESS")
        ApplicationStatus status,

        @Schema(description = "ID of the department handling the application",
                example = "123e4567-e89b-12d3-a456-426614174000")
        UUID departmentId
) {
}
