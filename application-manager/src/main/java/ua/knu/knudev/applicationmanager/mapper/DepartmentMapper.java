package ua.knu.knudev.applicationmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ua.knu.knudev.applicationmanager.domain.Department;
import ua.knu.knudev.applicationmanagerapi.dto.DepartmentDto;
import ua.knu.knudev.applicationmanagerapi.request.DepartmentCreateRequest;
import ua.knu.knudev.applicationmanagerapi.request.DepartmentUpdateRequest;
import ua.knu.knudev.icccommon.mapper.BaseMapper;

@Mapper(componentModel = "spring", uses = {MultiLanguageFieldMapper.class})
public interface DepartmentMapper extends BaseMapper<Department, DepartmentDto> {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Department fromCreateRequest(DepartmentCreateRequest request);

    void updateFromRequest(DepartmentUpdateRequest request, @MappingTarget Department department);
}

