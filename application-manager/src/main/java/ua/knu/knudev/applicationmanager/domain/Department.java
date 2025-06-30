package ua.knu.knudev.applicationmanager.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import ua.knu.knudev.icccommon.domain.embeddable.MultiLanguageField;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "department", schema = "application_manager")
@Builder
@Entity
public class Department {

    @Id
    @UuidGenerator
    private UUID id;

    @AttributeOverrides({
            @AttributeOverride(name = "en", column = @Column(name = "en_name")),
            @AttributeOverride(name = "uk", column = @Column(name = "uk_name"))
    })
    @Embedded
    @Column(nullable = false)
    private MultiLanguageField name;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "department_applications",
            schema = "application_manager",
            joinColumns = @JoinColumn(name = "department_id"),
            inverseJoinColumns = @JoinColumn(name = "application_id")
    )
    private Set<Application> applications;
}
