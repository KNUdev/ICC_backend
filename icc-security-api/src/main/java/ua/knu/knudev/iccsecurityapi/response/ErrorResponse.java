package ua.knu.knudev.iccsecurityapi.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Error response, contains error code and error message")
public class ErrorResponse {
    @Schema(description = "Error type", requiredMode = Schema.RequiredMode.REQUIRED, example = "404")
    private String error;

    @Schema(description = "Error message", requiredMode = Schema.RequiredMode.REQUIRED,
            example = "There was an unexpected error!")
    private String message;
}