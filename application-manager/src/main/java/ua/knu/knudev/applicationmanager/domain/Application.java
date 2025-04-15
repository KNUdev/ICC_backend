package ua.knu.knudev.applicationmanager.domain;

import ua.knu.knudev.applicationmanager.common.ApplicationStatus;
import ua.knu.knudev.applicationmanager.common.FullName;
import org.hibernate.annotations.UuidGenerator;
import ua.knu.knudev.applicationmanager.domain.embedded.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import ua.knu.knudev.employeemanager.domain.Employee;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "application", schema = "application_manager")
@Builder
@Entity
public class Application {

    @Id
    @UuidGenerator
    @Column (nullable = false, updatable = false)
    private UUID id;

    @Embedded
    private FullName applicantName;

    @Column(nullable = false)
    private String email;

    @Column (nullable = false)
    private LocalDateTime receivedAt;
    private LocalDateTime completedAt;

    @Column(nullable = false)
    private String problemDescription;

    private String problemPhoto;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "department_id", referencedColumnName = "id")
    private Department Department;

    @ElementCollection
    @CollectionTable(
            name = "application_assigned_employee_ids",
            schema = "application_manager",
            joinColumns = @JoinColumn(name = "application_id")
    )
    @Column(name = "assigned_employee_id")
    private Set<UUID> assignedEmployeeIds;
}
