package ua.knu.knudev.employeemanager.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.employeemanager.domain.Sector;
import ua.knu.knudev.employeemanagerapi.dto.SectorDto;
import ua.knu.knudev.icccommon.mapper.BaseMapper;

@Mapper(componentModel = "spring")
public interface SectorMapper extends BaseMapper<Sector, SectorDto> {
}
