package ua.knu.knudev.applicationmanager.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.applicationmanager.domain.embedded.MultiLanguageField;
import ua.knu.knudev.applicationmanagerapi.dto.MultiLanguageFieldDto;
import ua.knu.knudev.icccommon.mapper.BaseMapper;

@Mapper(componentModel = "spring")
public interface MultiLanguageFieldMapper extends BaseMapper<MultiLanguageField, MultiLanguageFieldDto> {
}
