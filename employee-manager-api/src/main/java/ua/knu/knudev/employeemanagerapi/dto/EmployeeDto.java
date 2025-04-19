package ua.knu.knudev.employeemanagerapi.dto;

import lombok.Builder;
import ua.knu.knudev.icccommon.constant.EmployeeAdministrativeRole;
import ua.knu.knudev.icccommon.dto.FullNameDto;
import ua.knu.knudev.icccommon.dto.WorkHoursDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record EmployeeDto(
        UUID id,
        FullNameDto name,
        String email,
        String phoneNumber,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Double salaryInUAH,
        Boolean isStudent,
        String avatar,
        LocalDate contractEndDate,
        WorkHoursDto workHours,
        EmployeeAdministrativeRole role,
        SpecialtyDto specialty,
        SectorDto sector
) {
}
