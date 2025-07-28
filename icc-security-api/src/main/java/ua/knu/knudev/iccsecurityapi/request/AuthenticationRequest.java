package ua.knu.knudev.iccsecurityapi.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request object for authenticating a user with email and password.")
public record AuthenticationRequest(
        @Schema(description = "The email address of the user", example = "user@knu.ua", requiredMode = Schema.RequiredMode.REQUIRED)
        String email,

        @Schema(description = "The password of the user", example = "Password123!", requiredMode = Schema.RequiredMode.REQUIRED)
        String password
) {
}
