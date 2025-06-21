package ua.knu.knudev.applicationmanagerapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Data Transfer Object representing a department")
public class DepartmentDto {

    @Schema(description = "Unique identifier of the department", example = "e7d15fc4-9f42-4c60-91e5-f2c8d2f8f2e4")
    private UUID id;

    @Schema(description = "Timestamp when the department was created", example = "2024-05-01T10:00:00")
    private LocalDateTime createdAt;

    @Valid
    @Schema(description = "MultiLanguageFieldDto for department name", implementation = MultiLanguageFieldDto.class)
    private MultiLanguageFieldDto name;

    @Schema(description = "Timestamp when the department was last updated", example = "2024-05-02T14:30:00")
    private LocalDateTime updatedAt;
}
