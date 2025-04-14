package ua.knu.knudev.employeemanagerapi.config.dto;

import lombok.*;
import ua.knu.knudev.icccommon.constant.SpecialtyCategory;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecialtyDto {
    private UUID id;
    private MultiLanguageFieldDto name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private SpecialtyCategory category;
    private Set<SectorDto> sectors = new HashSet<>();
}
