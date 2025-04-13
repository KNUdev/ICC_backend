package ua.knu.knudev.employeemanager.domain;

import jakarta.persistence.*;
import lombok.*;
import ua.knu.knudev.employeemanager.domain.embeddable.FullName;
import ua.knu.knudev.employeemanager.domain.embeddable.WorkHours;

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
    @GeneratedValue
    @Column(nullable = false)
    private UUID id;

    @Embedded
    private FullName name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private Double salaryInUAH;

    @Column(nullable = false)
    private Boolean isStudent;

    @Column
    private String avatar;

    @Embedded
    private WorkHours workHours;

    @ManyToOne
    @JoinColumn(name = "specialty_id", referencedColumnName = "id", nullable = false)
    private Specialty specialty;

    @ManyToOne
    @JoinColumn(name = "sector_id", referencedColumnName = "id", nullable = false)
    private Sector sector;
}
