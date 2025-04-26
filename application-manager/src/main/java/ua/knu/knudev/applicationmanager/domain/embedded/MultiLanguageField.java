package ua.knu.knudev.applicationmanager.domain.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class MultiLanguageField {

    @Column(nullable = false)
    private String en;

    @Column(nullable = false)
    private String uk;

    public MultiLanguageField() {
    }

    public MultiLanguageField(String en, String uk) {
        this.en = en;
        this.uk = uk;
    }
}
