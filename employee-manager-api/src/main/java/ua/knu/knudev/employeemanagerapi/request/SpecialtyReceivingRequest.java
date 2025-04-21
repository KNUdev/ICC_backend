package ua.knu.knudev.employeemanagerapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import ua.knu.knudev.icccommon.constant.SpecialtyCategory;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;

import java.time.LocalDateTime;

@Builder
@Schema(description = "Request DTO for receiving specialty details")
public record SpecialtyReceivingRequest(
        @Schema(description = "Substring which can be part of any specialty property with String type")
        String searchQuery,

        @Schema(description = "Name of the specialty sector")
        MultiLanguageFieldDto sectorName,

        @Schema(description = "Category of the specialty",
                implementation = SpecialtyCategory.class)
        SpecialtyCategory category,

        @Schema(description = "Date after which the specialty was created")
        LocalDateTime createdAfter,

        @Schema(description = "Date before which the specialty was created")
        LocalDateTime createdBefore,

        @Schema(description = "Date after which the specialty was updated")
        LocalDateTime updatedAfter,

        @Schema(description = "Date before which the specialty was created")
        LocalDateTime updatedBefore,

        @Schema(description = "Page number",
                implementation = Integer.class)
        Integer pageNumber,

        @Schema(description = "Number of sectors per page",
                implementation = Integer.class)
        Integer pageSize
) {
}
