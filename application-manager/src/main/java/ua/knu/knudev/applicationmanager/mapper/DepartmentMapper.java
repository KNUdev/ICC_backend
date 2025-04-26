package ua.knu.knudev.applicationmanager.mapper;

import ua.knu.knudev.applicationmanager.domain.Department;
import ua.knu.knudev.applicationmanager.domain.embedded.MultiLanguageField;
import ua.knu.knudev.applicationmanager.dto.DepartmentDto;

public class DepartmentMapper {

    public static Department toEntity(DepartmentDto dto) {
        return Department.builder()
                .id(dto.getId())
                .Name(new MultiLanguageField(dto.getNameEn(), dto.getNameUk()))
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

    public static DepartmentDto toDto(Department department) {
        return DepartmentDto.builder()
                .id(department.getId())
                .nameEn(department.getName().getEn())
                .nameUk(department.getName().getUk())
                .createdAt(department.getCreatedAt())
                .updatedAt(department.getUpdatedAt())
                .build();
    }
}
