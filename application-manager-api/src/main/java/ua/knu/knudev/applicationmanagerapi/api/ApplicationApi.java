package ua.knu.knudev.applicationmanagerapi.api;

import org.springframework.data.domain.Page;
import ua.knu.knudev.applicationmanagerapi.dto.ApplicationDto;
import ua.knu.knudev.applicationmanagerapi.request.*;

public interface ApplicationApi {
    ApplicationDto create(ApplicationCreateRequest request);

    ApplicationDto update(ApplicationUpdateRequest request);

    ApplicationDto getById(ApplicationGetByIdRequest request);

    Page<ApplicationDto> getAll(ApplicationGetAllRequest request);

    ApplicationDto delete(ApplicationDeleteRequest request);

    ApplicationDto addAssignedEmployee(ApplicationAddAssignedEmployeeRequest request);

    ApplicationDto removeAssignedEmployee(ApplicationRemoveAssignedEmployeeRequest request);
}
