package ua.knu.knudev.iccsecurityapi.dto;

import lombok.Builder;

@Builder
public record Tokens(String accessToken, String refreshToken) {
}

