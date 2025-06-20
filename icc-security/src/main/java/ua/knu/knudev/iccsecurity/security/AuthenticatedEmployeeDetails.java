package ua.knu.knudev.iccsecurity.security;

import org.springframework.security.core.userdetails.UserDetails;
import ua.knu.knudev.icccommon.constant.EmployeeAdministrativeRole;

import java.util.Set;
import java.util.UUID;

public interface AuthenticatedEmployeeDetails extends UserDetails {

    UUID getId();

    String getEmail();

    Set<EmployeeAdministrativeRole> getRoles();

}
