package ua.knu.knudev.iccrest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import ua.knu.knudev.fileserviceapi.api.GalleryItemServiceApi;
import ua.knu.knudev.fileserviceapi.dto.GalleryItemDto;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gallery")
public class GalleryItemController {

    private final GalleryItemServiceApi galleryItemServiceApi;

    @Operation(
            summary = "Retrieve gallery item",
            description = "Fetches detailed information about a sector based on the provided sector ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Gallery item was successfully retrieved",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GalleryItemDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Gallery item is not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/image/{itemId}")
    public GalleryItemDto getGalleryItemById(@PathVariable UUID itemId) {
        return galleryItemServiceApi.getById(itemId);
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Gallery items were successfully retrieved.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GalleryItemDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Gallery items are not found.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping
    public Page<GalleryItemDto> getAllGalleryItems
            (@RequestBody int pageNumber, int pageSize) {
        return galleryItemServiceApi.getAll(pageNumber, pageSize);
    }
}