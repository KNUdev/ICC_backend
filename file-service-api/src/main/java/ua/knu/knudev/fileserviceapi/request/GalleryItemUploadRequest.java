package ua.knu.knudev.fileserviceapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Schema(description = "Request object for uploading gallery itemw")
public record GalleryItemUploadRequest(
        @NotNull(message = "Item cannot be empty")
        @Schema(
                description = "Image which was uploaded",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        MultipartFile image,

        @NotNull(message = "Item cannot be without description")
        @Schema(
                description = "Image description"
        )
        String description
) {
}