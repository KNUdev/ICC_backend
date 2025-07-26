package ua.knu.knudev.applicationmanagerapi.api;

import org.springframework.data.domain.Page;
import ua.knu.knudev.applicationmanagerapi.dto.ApplicationDto;
import ua.knu.knudev.applicationmanagerapi.request.*;

import java.util.List;
import java.util.UUID;

public interface ApplicationApi {
    ApplicationDto create(ApplicationCreateRequest request);

    ApplicationDto createPrivateApplication(PrivateApplicationCreateRequest request);

    ApplicationDto update(ApplicationUpdateRequest request);

    ApplicationDto getById(UUID applicationId);

    Page<ApplicationDto> getAll(ApplicationGetAllRequest request);

    List<ApplicationDto> getByAssignedEmployeeId(UUID assignedEmployeeId);

    void delete(UUID applicationId);

    ApplicationDto addAssignedEmployee(ApplicationAddAssignedEmployeeRequest request);

    ApplicationDto removeAssignedEmployee(ApplicationRemoveAssignedEmployeeRequest request);
}
