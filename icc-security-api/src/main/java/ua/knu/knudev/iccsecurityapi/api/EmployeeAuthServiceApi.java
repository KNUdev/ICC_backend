package ua.knu.knudev.iccsecurityapi.api;

import ua.knu.knudev.iccsecurityapi.dto.AuthenticatedEmployeeDto;
import ua.knu.knudev.iccsecurityapi.request.AuthenticatedEmployeeUpdateRequest;
import ua.knu.knudev.iccsecurityapi.request.EmployeeRegistrationRequest;
import ua.knu.knudev.iccsecurityapi.response.EmployeeRegistrationResponse;

public interface EmployeeAuthServiceApi {

    EmployeeRegistrationResponse create(EmployeeRegistrationRequest request);

    boolean existsByEmail(String email);

    AuthenticatedEmployeeDto getByEmail(String email);

    void deleteByEmail(String email);

    void update(AuthenticatedEmployeeUpdateRequest request);


}
