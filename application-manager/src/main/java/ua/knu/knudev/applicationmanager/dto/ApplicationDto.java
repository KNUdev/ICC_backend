package ua.knu.knudev.applicationmanager.dto;

import lombok.*;
import ua.knu.knudev.applicationmanager.domain.embedded.ApplicationStatus;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationDto {
    private UUID id;
    private String applicantName;
    private String email;
    private LocalDateTime receivedAt;
    private LocalDateTime completedAt;
    private String problemDescription;
    private String problemPhoto;
    private ApplicationStatus status;
    private UUID departmentId;
    private Set<UUID> assignedEmployeeIds;
}
