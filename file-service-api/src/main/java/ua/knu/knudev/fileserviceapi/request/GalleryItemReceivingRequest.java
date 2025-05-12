package ua.knu.knudev.fileserviceapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Request DTO for receiving gallery item details")
public record GalleryItemReceivingRequest(
        @Schema(description = "Page number")
        Integer pageNumber,

        @Schema(description = "Number of images per page")
        Integer pageSize
) {
}