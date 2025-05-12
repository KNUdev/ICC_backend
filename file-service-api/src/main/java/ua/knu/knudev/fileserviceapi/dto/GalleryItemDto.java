package ua.knu.knudev.fileserviceapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Schema(description = "DTO that representing image gallery data")
public record GalleryItemDto(
        @Schema(description = "UUID format for image id", example = "1d87b3e3-44a7-4915-ac13-33180ed448ff")
        UUID creatorId,
        @Schema(description = "Gallery item name")
        String itemName,
        @Schema(description = "Description of gallery item")
        String description,
        @Schema(description = "Date when gallery item was uploaded", example = "2025-03-23T11:50:23.223")
        LocalDateTime uploadedAt,
        @Schema(description = "Date when gallery item was updated", example = "2025-03-23T11:50:23.223")
        LocalDateTime updatedAt
) {
}
