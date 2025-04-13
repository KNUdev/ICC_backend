package ua.knu.knudev.employeemanager.domain;

import jakarta.persistence.*;
import lombok.*;
import ua.knu.knudev.employeemanager.domain.embeddable.MultiLanguageField;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "employee_manager", name = "sector")
@Builder
public class Sector {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private UUID id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "en", column = @Column(name = "en_name")),
            @AttributeOverride(name = "uk", column = @Column(name = "uk_name"))
    })
    private MultiLanguageField name;

    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(
            name = "sector_specialty",
            joinColumns = @JoinColumn(name="sector_id"),
            inverseJoinColumns = @JoinColumn(name="specialty_id")
    )
    private Set<Specialty> specialties;
}
