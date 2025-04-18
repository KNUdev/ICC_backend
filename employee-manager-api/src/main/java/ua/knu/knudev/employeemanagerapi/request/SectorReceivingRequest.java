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

        @Schema(description = "Timestamp when Sector was created")
        LocalDateTime createdAt,

        @Schema(description = "Timestamp of the last update to Sector")
        LocalDateTime updatedAt,

        @Schema(
                description = "Page number",
                implementation = Integer.class
        )
        Integer pageNumber,

        @Schema(
                description = "Number of sectors per page",
                implementation = Integer.class
        )
        Integer pageSize
) {
}
