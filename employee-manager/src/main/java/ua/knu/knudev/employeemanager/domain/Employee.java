package ua.knu.knudev.employeemanager.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import ua.knu.knudev.employeemanager.domain.embeddable.FullName;
import ua.knu.knudev.employeemanager.domain.embeddable.WorkHours;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "employee_manager", name = "employee")
@Builder
public class Employee {
    @Id
    @UuidGenerator
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Embedded
    private FullName name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Double salaryInUAH;

    @Column(nullable = false)
    private Boolean isStudent;

    private String avatar;

    @Column(nullable = false)
    private LocalDate contractEndDate;

    @Embedded
    private WorkHours workHours;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeAdministrativeRole role;

    @ManyToOne
    @JoinColumn(name = "specialty_id", referencedColumnName = "id", nullable = false)
    private Specialty specialty;

    @ManyToOne
    @JoinColumn(name = "sector_id", referencedColumnName = "id", nullable = false)
    private Sector sector;
}
