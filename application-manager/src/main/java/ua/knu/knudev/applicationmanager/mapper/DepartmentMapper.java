package ua.knu.knudev.applicationmanager.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.applicationmanager.domain.Department;
import ua.knu.knudev.applicationmanagerapi.dto.DepartmentDto;
import ua.knu.knudev.icccommon.mapper.BaseMapper;

@Mapper(componentModel = "spring")
public interface DepartmentMapper extends BaseMapper<Department, DepartmentDto> {
}

