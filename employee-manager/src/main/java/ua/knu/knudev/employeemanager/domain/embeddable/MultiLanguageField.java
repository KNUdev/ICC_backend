package ua.knu.knudev.employeemanager.domain.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class MultiLanguageField {
    @Column(nullable = false)
    private String en;

    @Column(nullable = false)
    private String uk;
}
