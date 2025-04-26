package ua.knu.knudev.applicationmanager.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentDto {
    private UUID id;
    private String nameEn;
    private String nameUk;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
