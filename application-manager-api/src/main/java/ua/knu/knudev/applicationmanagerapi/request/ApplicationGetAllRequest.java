package ua.knu.knudev.applicationmanagerapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "ApplicationGetAllRequest", description = "Request for paginated list of applications")
public record ApplicationGetAllRequest(
        @Schema(description = "Page number", example = "0")
        int page,

        @Schema(description = "Page size", example = "10")
        int size
) {}
