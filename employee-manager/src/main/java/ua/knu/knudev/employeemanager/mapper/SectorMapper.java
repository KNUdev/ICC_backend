package ua.knu.knudev.employeemanager.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.employeemanager.domain.Sector;
import ua.knu.knudev.employeemanagerapi.config.dto.SectorDto;
import ua.knu.knudev.icccommon.mapper.BaseMapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface SectorMapper extends BaseMapper<Sector, SectorDto> {
    Sector toDomain(SectorDto sectorDto);

    SectorDto toDto(Sector sector);

    Set<Sector> toDomains(Set<SectorDto> sectorDtos);

}
