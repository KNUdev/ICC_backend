package ua.knu.knudev.fileserviceapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Builder
@Schema(description = "Request object for update a gallery item")
public record GalleryItemUpdateRequest(
        @Schema(
                description = "Image which was uploaded",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        MultipartFile item,
        @NotNull
        @Schema(
                description = "Item id",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        UUID itemId,
        @Schema(
                description = "Gallery item name",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String publicItemName,
        @Schema(
                description = "Gallery item ID",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String itemDescription
) {
}
