package ua.knu.knudev.applicationmanagerapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import ua.knu.knudev.applicationmanagerapi.dto.ApplicationDto;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;

import java.time.LocalDateTime;
import java.util.Set;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Builder
@Schema(name = "DepartmentGetAllRequest", description = "Pagination request for departments")
public record DepartmentGetAllRequest(
        @Schema(description = "Substring which can be part of department name")
        String searchQuery,

        @Schema(description = "Substring which can be part of problem description")
        String problemKeyword,

        @Schema(description = "Name of the applicant department")
        MultiLanguageFieldDto departmentName,

        @Schema(description = "Filters departments created before the specified timestamp")
        LocalDateTime createdBefore,

        @Schema(description = "Filters departments created after the specified timestamp")
        LocalDateTime createdAfter,

        @Schema(description = "Filters departments updated before the specified timestamp")
        LocalDateTime updatedBefore,

        @Schema(description = "Filters departments updated after the specified timestamp")
        LocalDateTime updatedAfter,

        @Schema(description = "List of applications of the department ")
        Set<ApplicationDto> applications,

        @Schema(description = "Page number", example = "0", requiredMode = REQUIRED)
        int pageNumber,

        @Schema(description = "Page size", example = "10", requiredMode = REQUIRED)
        int pageSize
) {
}