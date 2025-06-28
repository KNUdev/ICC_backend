package ua.knu.knudev.icccommon.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.icccommon.domain.embeddable.FullName;
import ua.knu.knudev.icccommon.dto.FullNameDto;

@Mapper(componentModel = "spring")
public interface FullNameMapper extends BaseMapper<FullName, FullNameDto> {
}
