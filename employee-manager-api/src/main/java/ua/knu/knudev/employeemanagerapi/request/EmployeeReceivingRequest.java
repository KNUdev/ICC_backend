package ua.knu.knudev.employeemanagerapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import ua.knu.knudev.icccommon.constant.EmployeeAdministrativeRole;
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

        @Schema(description = "Time of employee creation",
                implementation = LocalDateTime.class)
        LocalDateTime createdAt,

        @Schema(description = "Time of employee update",
                implementation = LocalDateTime.class)
        LocalDateTime updatedAt,

        @Schema(description = "Employee salary in UAH",
                implementation = Double.class)
        Double salaryInUAH,

        @Schema(description = "Indicates whether the employee is a student",
                implementation = Boolean.class)
        Boolean isStudent,

        @Schema(description = "Employee avatar")
        String avatar,

        @Schema(description = "End date of employee contract",
                implementation = LocalDate.class)
        LocalDate contractEndDate,

        @Schema(description = "Field which contains start and end time of work",
                implementation = WorkHoursDto.class)
        WorkHoursDto workHours,

        @Schema(description = "Employee administrative role",
                implementation = EmployeeAdministrativeRole.class)
        EmployeeAdministrativeRole role,

        @Schema(description = "Employee specialty name")
        String specialtyName,

        @Schema(description = "Employee sector name")
        String sectorName,

        @Schema(description = "Page number")
        Integer pageNumber,

        @Schema(description = "Number of employees per page")
        Integer pageSize
) {
}
