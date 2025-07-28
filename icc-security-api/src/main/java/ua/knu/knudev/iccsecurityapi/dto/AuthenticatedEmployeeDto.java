package ua.knu.knudev.iccsecurityapi.dto;

import lombok.Builder;
import ua.knu.knudev.icccommon.constant.EmployeeAdministrativeRole;

import java.util.UUID;

@Builder
public record AuthenticatedEmployeeDto(
        UUID id,
        String email,
        String password,
        EmployeeAdministrativeRole role,
        boolean enabled,
        boolean nonLocked
) {
}
