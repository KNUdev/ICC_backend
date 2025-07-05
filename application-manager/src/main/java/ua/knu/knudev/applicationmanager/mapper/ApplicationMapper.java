package ua.knu.knudev.applicationmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.knu.knudev.applicationmanager.domain.Application;
import ua.knu.knudev.applicationmanagerapi.dto.ApplicationDto;
import ua.knu.knudev.icccommon.mapper.BaseMapper;

@Mapper(componentModel = "spring")
public interface ApplicationMapper extends BaseMapper<Application, ApplicationDto> {
    @Override
    @Mapping(source = "department.id", target = "departmentId")
    ApplicationDto toDto(Application application);
}