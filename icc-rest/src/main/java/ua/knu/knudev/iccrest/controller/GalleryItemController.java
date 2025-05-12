package ua.knu.knudev.iccrest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import ua.knu.knudev.fileserviceapi.api.GalleryItemServiceApi;
import ua.knu.knudev.fileserviceapi.dto.GalleryItemDto;
import ua.knu.knudev.fileserviceapi.request.GalleryItemReceivingRequest;
import ua.knu.knudev.fileserviceapi.request.GalleryItemUpdateRequest;
import ua.knu.knudev.fileserviceapi.request.GalleryItemUploadRequest;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gallery")
public class GalleryItemController {

    private final GalleryItemServiceApi galleryItemServiceApi;

    @Operation(
            summary = "Create a new gallery item",
            description = """
                    This endpoint allows admin to create a new gallery item.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Gallery item successfully created.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GalleryItemDto.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input provided.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
            @ApiResponse(
                    responseCode = "403",
                    description = "You do not have access to this endpoint.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public GalleryItemDto uploadGalleryItem(
            @Valid @ModelAttribute @Parameter(
                    name = "Gallery item creation request",
                    description = "Gallery item creation details",
                    in = ParameterIn.QUERY,
                    required = true,
                    schema = @Schema(implementation = GalleryItemUploadRequest.class)
            ) GalleryItemUploadRequest galleryItemUploadRequest, @RequestParam UUID employeeId) {
        return galleryItemServiceApi.upload(galleryItemUploadRequest, employeeId);
    }

    @Operation(
            summary = "Manipulate with existing gallery item",
            description = """
                    This endpoint allows admin to delete, update or get existing gallery items.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Gallery item successfully retrieved.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GalleryItemDto.class)
                    )),
            @ApiResponse(
                    responseCode = "403",
                    description = "You do not have access to this endpoint.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "Gallery item not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    @PatchMapping("/update")
    public GalleryItemDto updateGalleryItem(
            @RequestBody @Parameter(
                    name = "Gallery item update request",
                    description = "Data to update gallery item",
                    schema = @Schema(implementation = GalleryItemUpdateRequest.class),
                    in = ParameterIn.QUERY
            ) GalleryItemUpdateRequest galleryItemUpdateRequest) {
        return galleryItemServiceApi.update(galleryItemUpdateRequest);
    }

    @DeleteMapping("/{creatorId}/delete")
    public void deleteGalleryItem(@PathVariable UUID creatorId) {
        galleryItemServiceApi.delete(creatorId);
    }

    @GetMapping("/{creatorId}")
    public GalleryItemDto getGalleryItemById(@PathVariable UUID creatorId) {
        return galleryItemServiceApi.getDtoById(creatorId);
    }

    @Parameters({
            @Parameter(name = "pageNumber", description = "Page number", example = "0"),
            @Parameter(name = "pageSize", description = "Number of sectors per page", example = "10")
    })
    @GetMapping("/getAll")
    public Page<GalleryItemDto> getAllGalleryItems
            (@RequestBody @Valid GalleryItemReceivingRequest galleryItemReceivingRequest) {
        return galleryItemServiceApi.getAll(galleryItemReceivingRequest);
    }
}