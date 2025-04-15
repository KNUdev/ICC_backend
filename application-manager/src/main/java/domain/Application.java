package domain;

import common.ApplicationStatus;
import common.FullName;
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
@Table(name = "application", schema = "public")
@Builder
@Entity
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Embedded
    private FullName applicantName;

    @Column(nullable = false)
    private String email;

    private LocalDateTime receivedAt;
    private LocalDateTime completedAt;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String problemDescription;

    private String problemPhoto;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Department applicantDepartment;

    @ManyToMany
    private Set<Employee> assignedEmployees;
}
