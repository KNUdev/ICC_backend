package ua.knu.knudev.employeemanagerapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;

import java.time.LocalDateTime;

@Builder
@Schema(description = "Request object for searching sectors")
public record SectorReceivingRequest(
        @Schema(description = "Substring which is a searchQuery")
        String searchQuery,

        @Schema(description = "en, uk names of the sector specialty")
        MultiLanguageFieldDto specialtyName,

        @Schema(description = "Filters entities created before the specified timestamp")
        LocalDateTime createdBefore,

        @Schema(description = "Filters entities created after the specified timestamp")
        LocalDateTime createdAfter,

        @Schema(description = "Filters entities updated before the specified timestamp")
        LocalDateTime updatedBefore,

        @Schema(description = "Filters entities updated after the specified timestamp")
        LocalDateTime updatedAfter,

        @Schema(description = "Page number")
        Integer pageNumber,

        @Schema(description = "Number of sectors per page")
        Integer pageSize
) {
}
