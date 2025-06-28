package ua.knu.knudev.employeemanager.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import ua.knu.knudev.icccommon.domain.embeddable.MultiLanguageField;

import java.time.LocalDateTime;
import java.util.HashSet;
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
    @UuidGenerator
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "en", column = @Column(name = "en_name")),
            @AttributeOverride(name = "uk", column = @Column(name = "uk_name"))
    })
    private MultiLanguageField name;

    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "sectors_specialties",
            schema = "employee_manager",
            joinColumns = @JoinColumn(name= "sector_id"),
            inverseJoinColumns = @JoinColumn(name= "specialty_id")
    )
    private Set<Specialty> specialties = new HashSet<>();

    public void addSpecialty(Specialty specialty) {
        this.specialties.add(specialty);
        specialty.getSectors().add(this);
    }

    public void addSpecialties(Set<Specialty> specialties) {
        for (Specialty specialty : specialties) {
            addSpecialty(specialty);
        }
    }
  
    public void removeAllSpecialties() {
        for (Specialty specialty : this.specialties) {
            specialty.getSectors().remove(this);
        }
        this.specialties.clear();
    }
}
