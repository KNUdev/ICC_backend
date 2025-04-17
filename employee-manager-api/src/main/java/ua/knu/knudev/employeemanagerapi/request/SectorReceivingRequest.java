package ua.knu.knudev.employeemanagerapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "Request object for searching sectors")
public record SectorReceivingRequest(
        @Schema(
                description = "Substring which can be part of any Sector property with String type",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String searchQuery,


        @Schema(
                description = "Time of Sector creation",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        LocalDateTime createdAt,

        @Schema(
                description = "Time of the last Sector update",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        LocalDateTime updatedAt,

        @Schema(
                description = "A set of sector specialties",
                requiredMode = Schema.RequiredMode.REQUIRED,
                implementation = Integer.class
        )
        Integer pageNumber,

        @Schema(
                description = "A set of sector specialties",
                requiredMode = Schema.RequiredMode.REQUIRED,
                implementation = Integer.class
        )
        Integer pageSize
) {
}
