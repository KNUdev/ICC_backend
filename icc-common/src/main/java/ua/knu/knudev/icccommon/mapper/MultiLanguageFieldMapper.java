package ua.knu.knudev.icccommon.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.icccommon.domain.embeddable.MultiLanguageField;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;

@Mapper(componentModel = "spring")
public interface MultiLanguageFieldMapper extends BaseMapper<MultiLanguageField, MultiLanguageFieldDto> {
}
