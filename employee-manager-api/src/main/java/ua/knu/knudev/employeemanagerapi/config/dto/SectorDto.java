package ua.knu.knudev.employeemanagerapi.config.dto;

import lombok.*;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SectorDto {
    private UUID id;
    private MultiLanguageFieldDto name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<SpecialtyDto> specialties = new HashSet<>();
}
