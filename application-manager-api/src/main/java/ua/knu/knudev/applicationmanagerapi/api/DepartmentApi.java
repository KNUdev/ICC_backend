package ua.knu.knudev.applicationmanagerapi.api;

import org.springframework.data.domain.Page;
import ua.knu.knudev.applicationmanagerapi.dto.DepartmentDto;
import ua.knu.knudev.applicationmanagerapi.request.*;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;

import java.util.UUID;

public interface DepartmentApi {
    DepartmentDto create(MultiLanguageFieldDto departmentName);

    DepartmentDto update(DepartmentUpdateRequest request);

    DepartmentDto getById(UUID departmentId);

    Page<DepartmentDto> getAll(DepartmentGetAllRequest request);

    void delete(UUID departmentId);
}