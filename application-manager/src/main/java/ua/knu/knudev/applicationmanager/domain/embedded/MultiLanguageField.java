package ua.knu.knudev.applicationmanager.domain.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class MultiLanguageField {
    @Column(nullable=false)
    private String en;

    @Column(nullable=false)
    private String uk;
}

