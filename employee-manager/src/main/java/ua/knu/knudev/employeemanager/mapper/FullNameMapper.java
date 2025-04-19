package ua.knu.knudev.employeemanager.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.employeemanager.domain.embeddable.FullName;
import ua.knu.knudev.icccommon.dto.FullNameDto;
import ua.knu.knudev.icccommon.mapper.BaseMapper;

@Mapper(componentModel = "spring")
public interface FullNameMapper extends BaseMapper<FullName, FullNameDto> {
}
