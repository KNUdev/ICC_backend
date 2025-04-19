package ua.knu.knudev.employeemanager.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.employeemanager.domain.Employee;
import ua.knu.knudev.employeemanagerapi.dto.EmployeeDto;
import ua.knu.knudev.icccommon.mapper.BaseMapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper extends BaseMapper<Employee, EmployeeDto> {
}
