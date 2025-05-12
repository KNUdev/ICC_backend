package ua.knu.knudev.employeemanagerapi.api;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import ua.knu.knudev.employeemanagerapi.dto.EmployeeDto;
import ua.knu.knudev.employeemanagerapi.request.EmployeeCreationRequest;
import ua.knu.knudev.employeemanagerapi.request.EmployeeReceivingRequest;
import ua.knu.knudev.employeemanagerapi.request.EmployeeUpdateRequest;

import java.util.UUID;

public interface EmployeeApi {
    EmployeeDto create(@Valid EmployeeCreationRequest request);

    EmployeeDto getById(UUID id);

    Page<EmployeeDto> getAll(EmployeeReceivingRequest request);

    void deleteById(UUID id);

    boolean existsById(UUID id);

    EmployeeDto update(@Valid EmployeeUpdateRequest request);
}
