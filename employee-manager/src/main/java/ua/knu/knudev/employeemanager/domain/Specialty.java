package ua.knu.knudev.employeemanager.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import ua.knu.knudev.icccommon.constant.SpecialtyCategory;
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
@Table(schema = "employee_manager", name = "specialty")
@Builder
public class Specialty {
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

    @Enumerated(EnumType.STRING)
    private SpecialtyCategory category;

    @ManyToMany(mappedBy = "specialties")
    private Set<Sector> sectors = new HashSet<>();

    public void addSector(Sector sector) {
        this.sectors.add(sector);
        sector.getSpecialties().add(this);
    }

    public void addSectors(Set<Sector> sectors) {
        for (Sector sector : sectors) {
            addSector(sector);
        }
    }

    public void removeAllSectors(Set<Sector> sectors) {

        for (Sector Sector : this.sectors) {
            Sector.getSpecialties().remove(this);
        }
        this.sectors.clear();
    }
}
