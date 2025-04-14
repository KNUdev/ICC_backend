package ua.knu.knudev.employeemanager.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.employeemanager.domain.Specialty;
import ua.knu.knudev.employeemanagerapi.config.dto.SpecialtyDto;
import ua.knu.knudev.icccommon.mapper.BaseMapper;

@Mapper(componentModel = "spring")
public interface SpecialtyMapper extends BaseMapper<Specialty, SpecialtyDto> {
    Specialty toDomain(SpecialtyDto specialtyDto);

    SpecialtyDto toDto(Specialty specialty);
}
