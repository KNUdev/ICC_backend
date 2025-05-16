package ua.knu.knudev.fileserviceapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Builder
@Schema(description = "Request object for uploading gallery item")
public record GalleryItemUploadRequest(
        @NotNull
        @Schema(
                description = "Id of person which uploaded the gallery item",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        UUID creatorId,
        @NotNull(message = "Item cannot be empty")
        @Schema(
                description = "Image which was uploaded",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        MultipartFile image,
        @NotNull(message = "Item name cannot be empty")
        @Schema(
                description = "Image which was uploaded",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String itemName,
        @Schema(
                description = "item description"
        )
        String itemDescription
) {
}