package ua.knu.knudev.employeemanagerapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import ua.knu.knudev.icccommon.constant.EmployeeAdministrativeRole;
import ua.knu.knudev.icccommon.dto.FullNameDto;
import ua.knu.knudev.icccommon.dto.WorkHoursDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Schema(description = "DTO representing employee data")
public record EmployeeDto(
        @Schema(description = "UUID format employee id", example = "1d87b3e3-44a7-4915-ac13-33180ed448ff")
        UUID id,
        @Schema(description = "Employee fullname")
        FullNameDto name,
        @Schema(description = "Email field", example = "ivan@knu.ua")
        String email,
        @Schema(description = "From 10 to 15 digits phone number", example = "380960222321")
        String phoneNumber,
        @Schema(description = "Datetime of employee creation", example = "2025-05-11T14:15:40")
        LocalDateTime createdAt,
        @Schema(description = "Datetime of employee update", example = "2025-05-11T14:15:40")
        LocalDateTime updatedAt,
        @Schema(description = "Employee salary in UAH", example = "18000")
        Double salaryInUAH,
        @Schema(description = "Field represents whether an employee is student", example = "true")
        Boolean isStudent,
        @Schema(description = "Field containing avatar filename", example = "hello.png")
        String avatar,
        @Schema(description = "Date of the end of employee contract", example = "2026-05-16")
        LocalDate contractEndDate,
        @Schema(description = "Employee work time")
        WorkHoursDto workHours,
        @Schema(description = "Employee administrative role", example = "COMMON_USER")
        EmployeeAdministrativeRole role,
        @Schema(description = "Specialty of the employee")
        SpecialtyDto specialty,
        @Schema(description = "Sector of the employee")
        SectorDto sector
) {
}
