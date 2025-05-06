package ua.knu.knudev.employeemanagerapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import ua.knu.knudev.icccommon.constant.EmployeeAdministrativeRole;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;
import ua.knu.knudev.icccommon.dto.WorkHoursDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Schema(description = "Request DTO for receiving specialty details")
public record EmployeeReceivingRequest(
        @Schema(description = "Substring which can be part of employee name")
        String searchQuery,

        @Schema(description = "Email of employee")
        String email,

        @Schema(description = "Phone number of employee")
        String phoneNumber,

        @Schema(description = "Filters employees created before the specified timestamp")
        LocalDateTime createdBefore,

        @Schema(description = "Filters employees created after the specified timestamp")
        LocalDateTime createdAfter,

        @Schema(description = "Filters employees updated before the specified timestamp")
        LocalDateTime updatedBefore,

        @Schema(description = "Filters employees updated after the specified timestamp")
        LocalDateTime updatedAfter,

        @Schema(description = "Employee salary in UAH")
        Double salaryInUAH,

        @Schema(description = "Indicates whether the employee is a student")
        Boolean isStudent,

        @Schema(description = "Employee avatar")
        String avatar,

        @Schema(description = "Filters contracts that end before the specified date")
        LocalDate contractEndDateBefore,

        @Schema(description = "Filters contracts that end after the specified date")
        LocalDate contractEndDateAfter,

        @Schema(description = "Field which contains start and end time of work",
                implementation = WorkHoursDto.class)
        WorkHoursDto workHours,

        @Schema(description = "Employee administrative role",
                implementation = EmployeeAdministrativeRole.class)
        EmployeeAdministrativeRole role,

        @Schema(description = "Employee specialty name")
        MultiLanguageFieldDto specialtyName,

        @Schema(description = "Employee sector name")
        MultiLanguageFieldDto sectorName,

        @Schema(description = "Page number")
        Integer pageNumber,

        @Schema(description = "Number of employees per page")
        Integer pageSize
) {
}
