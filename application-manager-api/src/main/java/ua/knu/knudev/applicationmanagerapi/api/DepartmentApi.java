package ua.knu.knudev.applicationmanagerapi.api;

import org.springframework.data.domain.Page;
import ua.knu.knudev.applicationmanagerapi.dto.DepartmentDto;
import ua.knu.knudev.applicationmanagerapi.request.*;

public interface DepartmentApi {
    DepartmentDto create(DepartmentCreateRequest request);

    DepartmentDto update(DepartmentUpdateRequest request);

    DepartmentDto getById(DepartmentGetByIdRequest request);

    Page<DepartmentDto> getAll(DepartmentGetAllRequest request);

    DepartmentDto delete(DepartmentDeleteRequest request);
}