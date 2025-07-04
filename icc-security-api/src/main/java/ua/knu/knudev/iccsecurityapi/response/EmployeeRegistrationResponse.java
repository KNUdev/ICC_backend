package ua.knu.knudev.iccsecurityapi.response;

import lombok.Builder;
import ua.knu.knudev.icccommon.constant.EmployeeAdministrativeRole;

import java.util.UUID;

@Builder
public record EmployeeRegistrationResponse(
        UUID id,
        String email,
        EmployeeAdministrativeRole role
) {
}
