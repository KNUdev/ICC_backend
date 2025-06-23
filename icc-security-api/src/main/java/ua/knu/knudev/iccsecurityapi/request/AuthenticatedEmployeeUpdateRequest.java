package ua.knu.knudev.iccsecurityapi.request;

import lombok.Builder;
import ua.knu.knudev.icccommon.constant.EmployeeAdministrativeRole;

import java.util.UUID;

@Builder
public record AuthenticatedEmployeeUpdateRequest(
       UUID employeeId,
       String email,
       String oldPassword,
       String newPassword,
       EmployeeAdministrativeRole role,
       boolean isAdminUsage
) {
}
