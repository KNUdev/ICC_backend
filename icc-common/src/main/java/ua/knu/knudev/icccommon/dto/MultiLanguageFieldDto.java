package ua.knu.knudev.icccommon.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Schema(description = "Represents a fields in multiple languages: English and Ukrainian.")
@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class MultiLanguageFieldDto {
    @Schema(description = "The field in English.", example = "Historical")
    @NotEmpty(message = "English field cannot be blank or empty.")
    @Pattern(regexp = "^[A-Za-z\\s-]+$",
            message = "English field contains only english-alphabet letters"
    )
    private String en;

    @Schema(description = "The field in Ukrainian.", example = "Історичний")
    @NotBlank(message = "Ukrainian field cannot be blank or empty.")
    @Pattern(regexp = "^[А-Яа-яЇїІіЄєҐґ\\s'’-]+$",
            message = "Ukrainian field contains only ukrainian-alphabet letters"
    )
    private String uk;
}
