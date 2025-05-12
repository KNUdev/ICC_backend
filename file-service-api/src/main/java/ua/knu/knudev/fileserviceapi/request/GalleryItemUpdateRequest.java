package ua.knu.knudev.fileserviceapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Builder
@Schema(description = "Request object for update a gallery item")
public record GalleryItemUpdateRequest(
        @NotNull(message = "Item cannot be empty")
        @Schema(
                description = "Image which was uploaded",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        MultipartFile image,
        @NotNull(message = "ID cannot be empty")
        @Schema(
                description = "Gallery item ID",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        UUID creatorID,
        @NotNull(message = "Item cannot be without name")
        @Schema(
                description = "Gallery item name",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String itemName,
        @NotNull(message = "Item cannot be without description")
        @Schema(
                description = "Gallery item ID",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String description
) {
}
