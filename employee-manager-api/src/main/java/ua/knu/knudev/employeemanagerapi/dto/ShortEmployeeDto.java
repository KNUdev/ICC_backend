package ua.knu.knudev.employeemanagerapi.dto;

import lombok.Builder;
import ua.knu.knudev.icccommon.dto.FullNameDto;

import java.time.LocalDateTime;

@Builder
public record ShortEmployeeDto (
        LocalDateTime updatedAt,
        String email,
        FullNameDto fullNameDto
) {
}
