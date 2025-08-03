package ua.knu.knudev.iccsecurity.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ua.knu.knudev.icccommon.constant.EmployeeAdministrativeRole;
import ua.knu.knudev.iccsecurity.exception.AccountAuthException;
import ua.knu.knudev.iccsecurity.security.AuthenticatedEmployeeDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(schema = "security_manager", name = "authenticated_employee")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticatedEmployee implements Serializable, AuthenticatedEmployeeDetails {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, name = "administrative_role")
    @Enumerated(EnumType.STRING)
    private EmployeeAdministrativeRole role;

    @Column(name = "is_enabled", nullable = false)
    private boolean enabled;

    @Column(name = "is_non_locked", nullable = false)
    private boolean nonLocked;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.of(role)
                .filter(Objects::nonNull)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getUsername() {
        throw new AccountAuthException("Account does not have username");
    }

    @Override
    public boolean isAccountNonLocked() {
        return nonLocked;
    }

    @Override
    public Set<EmployeeAdministrativeRole> getRoles() {
        return Set.of(role);
    }
}
