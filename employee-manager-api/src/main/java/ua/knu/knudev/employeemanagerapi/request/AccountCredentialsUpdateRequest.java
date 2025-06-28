package ua.knu.knudev.employeemanagerapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.util.UUID;

@Builder
public record AccountCredentialsUpdateRequest(
        @NotBlank(message = "Id can`t be blank or empty")
        UUID id,

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
        String oldPassword,

        String newPassword,

        @Schema(description = "User`s email address", example = "john@knu.ua", requiredMode = Schema.RequiredMode.REQUIRED)
        String email
) {
}
