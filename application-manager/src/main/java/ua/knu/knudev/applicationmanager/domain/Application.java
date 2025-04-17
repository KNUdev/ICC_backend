package ua.knu.knudev.applicationmanager.domain;

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

    @Column (nullable = false)
    private String applicantName;

    @Column (nullable = false, updatable = false)
    private String email;

    @Column (nullable = false)
    private LocalDateTime receivedAt;
    @Column (nullable = false)
    private LocalDateTime completedAt;

    @Column(nullable = false)
    private String problemDescription;

    @Column(nullable = false)
    private String problemPhoto;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "department_id", referencedColumnName = "id")
    private Department Department;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "application_employee",
            schema = "application_manager",
            joinColumns = @JoinColumn(name = "application_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private Set<Employee> assignedEmployees;

}
