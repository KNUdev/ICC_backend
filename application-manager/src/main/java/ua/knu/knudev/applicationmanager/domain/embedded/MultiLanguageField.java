package ua.knu.knudev.applicationmanager.domain.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class MultiLanguageField {
    @Column(nullable = false)
    private String en;

    @Column(nullable = false)
    private String uk;
}
