package ua.knu.knudev.applicationmanagerapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MultiLanguageFieldDto {

    @Schema(
            description = "Text in English",
            example = "Hello world",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "This field cannot be null")
    private String en;

    @Schema(
            description = "Text in Ukrainian",
            example = "Привіт світ",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "This field cannot be null")
    private String uk;
}
