package ua.knu.knudev.employeemanagerapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "Request object for searching sectors")
public record SectorReceivingRequest(
        @Schema(description = "Substring which can be part of any Sector property with String type")
        String searchQuery,

        @Schema(description = "Name of the sector specialty")
        String specialtyName,

        @Schema(description = "Time of Sector creation")
        LocalDateTime createdAt,

        @Schema(description = "Time of the last Sector update")
        LocalDateTime updatedAt,

        @Schema(
                description = "A set of sector specialties",
                implementation = Integer.class
        )
        Integer pageNumber,

        @Schema(
                description = "A set of sector specialties",
                implementation = Integer.class
        )
        Integer pageSize
) {
}
