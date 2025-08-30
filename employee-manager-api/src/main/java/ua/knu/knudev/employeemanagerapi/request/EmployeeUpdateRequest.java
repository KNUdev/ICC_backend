package ua.knu.knudev.employeemanagerapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;
import ua.knu.knudev.icccommon.constant.EmployeeAdministrativeRole;
import ua.knu.knudev.icccommon.dto.FullNameDto;
import ua.knu.knudev.icccommon.dto.WorkHoursDto;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Schema(description = "Request object for updating an employee")
public record EmployeeUpdateRequest(
        @NotNull
        @Schema(
                description = "Employee id",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        UUID id,

        @Valid
        @Schema(
                description = "Employee full name",
                implementation = FullNameDto.class
        )
        FullNameDto fullName,

        @Schema(
                description = "Employee email"
        )
        String email,

        @Schema(
                description = "Employee password"
        )
        String password,

        @Schema(
                description = "Employee phone number"
        )
        String phoneNumber,

        @Schema(
                description = "Employee salary in UAH"
        )
        Double salaryInUAH,

        @Schema(
                description = "Indicates whether the employee is a student"
        )
        Boolean isStudent,

        @Schema(
                description = "Employee avatar file"
        )
        MultipartFile avatarFile,

        @Schema(
                description = "End date of employee contract"
        )
        LocalDate contractEndDate,

        @Schema(
                description = "Field which contains start and end time of work",
                implementation = WorkHoursDto.class
        )
        WorkHoursDto workHours,

        @Schema(
                description = "Employee administrative role",
                implementation = EmployeeAdministrativeRole.class
        )
        EmployeeAdministrativeRole role,

        @Schema(
                description = "Employee specialty"
        )
        UUID specialtyId,

        @Schema(
                description = "Employee sector"
        )
        UUID sectorId
) {
}
