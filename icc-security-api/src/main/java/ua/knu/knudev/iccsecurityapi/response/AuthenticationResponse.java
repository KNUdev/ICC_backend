package ua.knu.knudev.iccsecurityapi.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Authentication response")
public record AuthenticationResponse(
        @Schema(description = "Authentication access token", requiredMode = Schema.RequiredMode.REQUIRED)
        String accessToken,

        @Schema(description = "Refresh token", requiredMode = Schema.RequiredMode.REQUIRED)
        String refreshToken
) {
}

