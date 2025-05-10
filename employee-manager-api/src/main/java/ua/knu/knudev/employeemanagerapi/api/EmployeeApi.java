package ua.knu.knudev.employeemanagerapi.api;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import ua.knu.knudev.employeemanagerapi.dto.EmployeeDto;
import ua.knu.knudev.employeemanagerapi.request.EmployeeCreationRequest;
import ua.knu.knudev.employeemanagerapi.request.EmployeeReceivingRequest;
import ua.knu.knudev.employeemanagerapi.request.EmployeeUpdateRequest;

import java.util.UUID;

public interface EmployeeApi {
    EmployeeDto create(EmployeeCreationRequest request);

    EmployeeDto getById(UUID id);

    Page<EmployeeDto> getAll(EmployeeReceivingRequest request);

    void deleteById(UUID id);

    boolean existsById(UUID id);

    EmployeeDto update(EmployeeUpdateRequest request);

    String updateAvatar(UUID id, MultipartFile avatarFile);

    void removeAvatar(UUID employeeId);
}
