package ua.knu.knudev.employeemanager.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.employeemanager.domain.embeddable.MultiLanguageField;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;
import ua.knu.knudev.icccommon.mapper.BaseMapper;

@Mapper(componentModel = "spring")
public interface MultiLanguageFieldMapper extends BaseMapper<MultiLanguageField, MultiLanguageFieldDto>{

}
