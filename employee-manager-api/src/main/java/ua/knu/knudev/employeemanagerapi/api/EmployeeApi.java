package ua.knu.knudev.employeemanagerapi.api;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import ua.knu.knudev.employeemanagerapi.dto.EmployeeDto;
import ua.knu.knudev.employeemanagerapi.request.EmployeeCreationRequest;
import ua.knu.knudev.employeemanagerapi.request.EmployeeReceivingRequest;
import ua.knu.knudev.employeemanagerapi.request.EmployeeUpdateRequest;
import ua.knu.knudev.employeemanagerapi.response.GetEmployeeResponse;

import java.util.UUID;

public interface EmployeeApi {
    EmployeeDto create(@Valid EmployeeCreationRequest request);

    GetEmployeeResponse getById(UUID id);

    Page<GetEmployeeResponse> getAll(EmployeeReceivingRequest request);

    void deleteById(UUID id);

    boolean existsById(UUID id);

    EmployeeDto update(@Valid EmployeeUpdateRequest request);

    String addAvatar(MultipartFile file);

    String updateAvatar(UUID id, MultipartFile avatarFile);

    void removeAvatar(UUID employeeId);
}
