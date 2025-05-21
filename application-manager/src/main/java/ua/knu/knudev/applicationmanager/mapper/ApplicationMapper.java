package ua.knu.knudev.applicationmanager.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.applicationmanager.domain.Application;
import ua.knu.knudev.applicationmanagerapi.dto.ApplicationDto;
import ua.knu.knudev.icccommon.mapper.BaseMapper;

@Mapper(componentModel = "spring")
public interface ApplicationMapper extends BaseMapper<Application, ApplicationDto> {
}