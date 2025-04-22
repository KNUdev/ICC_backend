package ua.knu.knudev.employeemanagerapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import ua.knu.knudev.employeemanagerapi.dto.SectorDto;
import ua.knu.knudev.employeemanagerapi.dto.SpecialtyDto;
import ua.knu.knudev.icccommon.constant.EmployeeAdministrativeRole;
import ua.knu.knudev.icccommon.dto.FullNameDto;
import ua.knu.knudev.icccommon.dto.WorkHoursDto;

import java.time.LocalDate;

@Builder
@Schema(description = "Request object for creating an employee")
public record EmployeeCreationRequest(
        @NotNull(message = "Field 'fullName' cannot be null")
        @Valid
        @Schema(
                description = "Employee full name",
                requiredMode = Schema.RequiredMode.REQUIRED,
                implementation = FullNameDto.class
        )
        FullNameDto fullName,

        @NotNull(message = "Field 'email' cannot be null")
        @Schema(
                description = "Employee email",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Pattern(
                regexp = "^[\\w.-]+@knu\\.ua$",
                message = "Email must be a valid knu.ua address"
        )
        String email,

        @NotNull(message = "Field 'phoneNumber' cannot be null")
        @Schema(
                description = "Employee phone number",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Pattern(
                regexp = "^\\d{10}$",
                message = "Phone number must be exactly 10 digits"
        )
        String phoneNumber,

        @NotNull(message = "Field 'salaryInUAH' cannot be null")
        @Schema(
                description = "Employee salary in UAH",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Double salaryInUAH,

        @NotNull(message = "Field 'isStudent' cannot be null")
        @Schema(
                description = "Indicates whether the employee is a student",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Boolean isStudent,

        @Schema(
                description = "Employee avatar",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        String avatar,

        @NotNull(message = "Field 'contractEndDate' cannot be null")
        @Schema(
                description = "End date of employee contract",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        LocalDate contractEndDate,

        @NotNull(message = "Field 'workHours' cannot be null")
        @Schema(
                description = "Field which contains start and end time of work",
                requiredMode = Schema.RequiredMode.REQUIRED,
                implementation = WorkHoursDto.class
        )
        WorkHoursDto workHours,

        @NotNull(message = "Field 'role' cannot be null")
        @Schema(
                description = "Employee administrative role",
                requiredMode = Schema.RequiredMode.REQUIRED,
                implementation = EmployeeAdministrativeRole.class
        )
        EmployeeAdministrativeRole role,

        @NotNull(message = "Field 'specialty' cannot be null")
        @Schema(
                description = "Employee specialty",
                requiredMode = Schema.RequiredMode.REQUIRED,
                implementation = SpecialtyDto.class
        )
        SpecialtyDto specialty,

        @NotNull(message = "Field 'sector' cannot be null")
        @Schema(
                description = "Employee sector",
                requiredMode = Schema.RequiredMode.REQUIRED,
                implementation = SectorDto.class
        )
        SectorDto sector
) {
}
