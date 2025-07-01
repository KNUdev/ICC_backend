package ua.knu.knudev.applicationmanagerapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;
import ua.knu.knudev.icccommon.domain.embeddable.FullName;
import ua.knu.knudev.icccommon.dto.FullNameDto;
import ua.knu.knudev.icccommon.enums.ApplicationStatus;

import java.util.UUID;

@Builder
@Schema(name = "ApplicationCreateRequest", description = "Object containing data required to create a new application")
public record ApplicationCreateRequest(

        @Schema(description = "Name of the applicant",
                example = "Jane Smith",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "This field cannot be null")
        FullNameDto applicantName,

        @Schema(description = "Email of the applicant",
                example = "jane.smith@example.com",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "This field cannot be null")
        String applicantEmail,

        @Schema(description = "Description of the reported problem",
                example = "Air conditioning not working in office",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "This field cannot be null")
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

        @Schema(description = "Initial status of the application",
                example = "PENDING",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "This field cannot be null")
        ApplicationStatus status,

        @Schema(description = "Department ID responsible for handling the application",
                example = "123e4567-e89b-12d3-a456-426614174000",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "This field cannot be null")
        UUID departmentId
) {
}
