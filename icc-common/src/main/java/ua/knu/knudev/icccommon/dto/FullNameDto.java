package ua.knu.knudev.icccommon.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Schema(description = "Represents full name with first, middle, last names.")
@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class FullNameDto {
    @Schema(description = "First name")
    @NotBlank(message = "First name field cannot be empty.")
    private String firstName;

    @Schema(description = "Middle name")
    @NotBlank(message = "Middle name field cannot be empty.")
    private String middleName;

    @Schema(description = "Last name")
    @NotBlank(message = "Last name field cannot be empty.")
    private String lastName;
}
