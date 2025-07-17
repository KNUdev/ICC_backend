package ua.knu.knudev.applicationmanagerapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import ua.knu.knudev.icccommon.dto.FullNameDto;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;
import ua.knu.knudev.icccommon.enums.ApplicationStatus;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
@Schema(name = "ApplicationGetAllRequest", description = "Request for paginated list of applications")
public record ApplicationGetAllRequest(
        @Schema(description = "Substring which can be part of applicant name")
        String searchQuery,

        @Schema(description = "Name of the person who submitted the application")
        FullNameDto applicantName,

        @Schema(description = "Email of the person who submitted the application")
        String applicantEmail,

        @Schema(description = "Filters applications received before the specified timestamp")
        LocalDateTime receivedBefore,

        @Schema(description = "Filters applications received after the specified timestamp")
        LocalDateTime receivedAfter,

        @Schema(description = "Filters applications completed before the specified timestamp")
        LocalDateTime completedBefore,

        @Schema(description = "Filters applications completed after the specified timestamp")
        LocalDateTime completedAfter,

        @Schema(description = "Description of the problem")
        String problemDescription,

        @Schema(description = "Photo of the problem")
        String problemPhoto,

        @Schema(description = "Application status", implementation = ApplicationStatus.class)
        ApplicationStatus status,

        @Schema(description = "Name of the department to which the applicant belongs", implementation = MultiLanguageFieldDto.class)
        MultiLanguageFieldDto departmentName,

        @Schema(description = "ID set of the assigned employee to the application")
        Set<UUID> assignedEmployeeIds,

        @Schema(description = "Page number", example = "0", implementation = Integer.class)
        Integer pageNumber,

        @Schema(description = "Page size", example = "10", implementation = Integer.class)
        Integer pageSize,

        @Schema(description = "Filters private or non-private applications")
        Boolean isPrivate
) {
}
