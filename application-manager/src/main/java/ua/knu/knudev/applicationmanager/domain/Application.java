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

    @Embedded
    @Column (nullable = false)
    private String applicantName;

    @Column (nullable = false, updatable = false)
    private String email;

    private LocalDateTime receivedAt;
    private LocalDateTime completedAt;

    @Column(nullable = false)
    private String problemDescription;

    private String problemPhoto;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "applicant_department_id", referencedColumnName = "id")
    private Department applicantDepartment;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "application_employee",
            joinColumns = @JoinColumn(name = "application_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private Set<Employee> assignedEmployees;

}
