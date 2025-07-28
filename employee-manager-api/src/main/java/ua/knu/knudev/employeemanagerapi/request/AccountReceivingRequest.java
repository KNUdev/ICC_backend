package ua.knu.knudev.employeemanagerapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record AccountReceivingRequest(
        @Schema(description = "User`s email address", example = "john@knu.ua", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Email can`t be blank or empty")
        @Email(message = "The string must be in email format")
        @Pattern(
                regexp = "^[\\w.-]+@knu\\.ua$",
                message = "Email must be in @knu.ua domain"
        )
        String email,

        @Schema(description = "User`s password", example = "QwerTy123!", requiredMode = Schema.RequiredMode.REQUIRED,
                minLength = 8, maxLength = 64)
        @NotBlank(message = "Password can`t be blank or empty")
        @Size(
                min = 8,
                max = 64,
                message = "Password size must be in the gap from 8 to 64 symbols"
        )
        @Pattern(
                regexp = "^(?=.*[a-zA-Z])(?=.*\\d).*$",
                message = "Password must contains only allowed symbols"
        )
        String password
) {
}
