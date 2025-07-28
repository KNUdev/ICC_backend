package ua.knu.knudev.iccsecurity.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.icccommon.mapper.BaseMapper;
import ua.knu.knudev.iccsecurity.domain.AuthenticatedEmployee;
import ua.knu.knudev.iccsecurityapi.dto.AuthenticatedEmployeeDto;

@Mapper(componentModel = "spring")
public interface AuthenticatedEmployeeMapper extends BaseMapper<AuthenticatedEmployee, AuthenticatedEmployeeDto> {
}
