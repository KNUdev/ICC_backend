package ua.knu.knudev.employeemanagerapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import ua.knu.knudev.icccommon.constant.SpecialtyCategory;

import java.time.LocalDateTime;

@Builder
@Schema(description = "Request DTO for receiving specialty details")
public record SpecialtyReceivingRequest(
        @Schema(description = "Substring which can be part of any specialty property with String type")
        String searchQuery,

        @Schema(description = "Name of the specialty sector")
        String sectorName,

        @Schema(description = "Category of the specialty",
                implementation = SpecialtyCategory.class)
        SpecialtyCategory category,

        @Schema(description = "Date when specialty created")
        LocalDateTime createdAt,

        @Schema(description = "Date when specialty updated")
        LocalDateTime updatedAt,

        @Schema(description = "Page number",
                implementation = Integer.class)
        Integer pageNumber,

        @Schema(description = "Number of sectors per page",
                implementation = Integer.class)
        Integer pageSize
) {
}
