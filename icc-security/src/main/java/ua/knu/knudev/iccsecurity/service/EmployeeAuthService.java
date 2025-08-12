package ua.knu.knudev.iccsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.knu.knudev.icccommon.constant.EmployeeAdministrativeRole;
import ua.knu.knudev.iccsecurity.domain.AuthenticatedEmployee;
import ua.knu.knudev.iccsecurity.exception.AccountAuthException;
import ua.knu.knudev.iccsecurity.mapper.AuthenticatedEmployeeMapper;
import ua.knu.knudev.iccsecurity.repository.AuthenticatedEmployeeRepository;
import ua.knu.knudev.iccsecurityapi.api.EmployeeAuthServiceApi;
import ua.knu.knudev.iccsecurityapi.dto.AuthenticatedEmployeeDto;
import ua.knu.knudev.iccsecurityapi.request.AuthenticatedEmployeeUpdateRequest;
import ua.knu.knudev.iccsecurityapi.request.EmployeeRegistrationRequest;
import ua.knu.knudev.iccsecurityapi.response.EmployeeRegistrationResponse;

import java.util.Optional;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class EmployeeAuthService implements EmployeeAuthServiceApi {

    private final AuthenticatedEmployeeRepository authenticatedEmployeeRepository;
    private final AuthenticatedEmployeeMapper authenticatedEmployeeMapper;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public EmployeeRegistrationResponse create(EmployeeRegistrationRequest request) {
        AuthenticatedEmployee authenticatedEmployee = AuthenticatedEmployee.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role())
                .build();
        setAuthenticatedEmployeeDefaults(authenticatedEmployee);

        AuthenticatedEmployee savedAuthenticatedEmployee = authenticatedEmployeeRepository.save(authenticatedEmployee);
        return EmployeeRegistrationResponse.builder()
                .id(savedAuthenticatedEmployee.getId())
                .email(savedAuthenticatedEmployee.getEmail())
                .role(savedAuthenticatedEmployee.getRole())
                .build();
    }

    @Override
    public boolean existsByEmail(String email) {
        return authenticatedEmployeeRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByRole(EmployeeAdministrativeRole role) {
        return authenticatedEmployeeRepository.existsByRole(role);
    }

    @Override
    public void update(AuthenticatedEmployeeUpdateRequest request) {
        AuthenticatedEmployee employee = authenticatedEmployeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new AccountAuthException("Employee with id " + request.employeeId() + " does not exist"));

        String newEmail = request.email();

        checkIfIsAdminUsage(request, newEmail);
        updatePassword(request, employee);

        if (newEmail != null && (newEmail.matches("^[\\w.-]+@knu\\.ua$") &&
                !authenticatedEmployeeRepository.existsByEmail(newEmail))) {
            updateField(newEmail, employee::setEmail);
        }

        updateField(request.role(), employee::setRole);

        authenticatedEmployeeRepository.save(employee);
    }

    @Override
    public AuthenticatedEmployeeDto getByEmail(String email) {
        AuthenticatedEmployee authenticatedEmployee = getDomainByEmail(email).orElseThrow(
                () -> new AccountAuthException("Employee with email " + email + " does not exist"));

        return authenticatedEmployeeMapper.toDto(authenticatedEmployee);
    }

    @Override
    public void deleteByEmail(String email) {
        getDomainByEmail(email).ifPresent(authenticatedEmployee ->
                authenticatedEmployeeRepository.deleteById(authenticatedEmployee.getId()));
    }

    private void updatePassword(AuthenticatedEmployeeUpdateRequest request, AuthenticatedEmployee employee) {
        if (request.newPassword() != null) {
            String encodedNewPassword = passwordEncoder.encode(request.newPassword());
            if (request.newPassword().matches("^(?=.*[a-zA-Z])(?=.*\\d).*$")) {
                updateField(encodedNewPassword, employee::setPassword);
            }
        }
    }

    private void checkIfIsAdminUsage(AuthenticatedEmployeeUpdateRequest request, String newEmail) {
        if (!request.isAdminUsage()) {
            boolean isAuthenticated = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            newEmail,
                            request.oldPassword())
            ).isAuthenticated();

            if (!isAuthenticated) {
                throw new AccountAuthException("Credentials is mismatch!");
            }
        }
    }

    public Optional<AuthenticatedEmployee> getDomainByEmail(String email) {
        return authenticatedEmployeeRepository.findByEmail(email);
    }

    private <T> void updateField(T newValue, Consumer<T> setter) {
        Optional.ofNullable(newValue).ifPresent(setter);
    }

    private void setAuthenticatedEmployeeDefaults(AuthenticatedEmployee authenticatedEmployee) {
        authenticatedEmployee.setNonLocked(true);
        authenticatedEmployee.setEnabled(true);
    }
}
