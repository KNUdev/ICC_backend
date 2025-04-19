package ua.knu.knudev.employeemanager.mapper;


import org.mapstruct.Mapper;
import ua.knu.knudev.employeemanager.domain.embeddable.WorkHours;
import ua.knu.knudev.icccommon.dto.WorkHoursDto;
import ua.knu.knudev.icccommon.mapper.BaseMapper;

@Mapper(componentModel = "spring")
public interface WorkHoursMapper extends BaseMapper<WorkHours, WorkHoursDto> {
}
