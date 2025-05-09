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
import ua.knu.knudev.employeemanagerapi.api.SpecialtyApi;
import ua.knu.knudev.employeemanagerapi.dto.SpecialtyDto;
import ua.knu.knudev.employeemanagerapi.request.SpecialtyCreationRequest;
import ua.knu.knudev.employeemanagerapi.request.SpecialtyReceivingRequest;
import ua.knu.knudev.employeemanagerapi.request.SpecialtyUpdateRequest;
import ua.knu.knudev.icccommon.constant.SpecialtyCategory;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/specialty")
public class SpecialtyController {

    private final SpecialtyApi specialtyApi;

    @Operation(
            summary = "Create a new specialty",
            description = """
                    This endpoint allows admin to create a new specialty.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Specialty successfully created.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SpecialtyDto.class)
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
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public SpecialtyDto createSpecialty(
            @Valid @ModelAttribute @Parameter(
                    name = "Specialty creation request",
                    description = "Specialty creation details",
                    in = ParameterIn.QUERY,
                    required = true,
                    schema = @Schema(implementation = SpecialtyCreationRequest.class)
            ) SpecialtyCreationRequest specialtyCreationRequest) {
        return specialtyApi.create(specialtyCreationRequest);
    }

    @Operation(
            summary = "Manipulate with existing specialty",
            description = """
                    This endpoint allows admin to delete, update or get existing specialties.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Specialty successfully retrieved.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SpecialtyDto.class)
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
                    description = "Specialty not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    @PatchMapping("/update")
    public SpecialtyDto updateSpecialty(
            @RequestBody @Parameter(
                    name = "SpecialtyUpdateRequest",
                    description = "Data to update specialty",
                    schema = @Schema(implementation = SpecialtyUpdateRequest.class),
                    in = ParameterIn.QUERY
            ) SpecialtyUpdateRequest specialtyUpdateRequest) {
        return specialtyApi.update(specialtyUpdateRequest);
    }

    @DeleteMapping("/{specialtyId}/delete")
    public void deleteSpecialty(@PathVariable UUID specialtyId) {
        specialtyApi.delete(specialtyId);
    }

    @GetMapping("/{specialtyId}")
    public SpecialtyDto getSpecialtyById(@PathVariable UUID specialtyId) {
        return specialtyApi.getById(specialtyId);
    }

    @Parameters({
            @Parameter(name = "searchQuery", description = "Search term to filter specialties by name or date.", example = "Engineer"),
            @Parameter(name = "sectorName", description = "Name of the specialty sector.", example = "1-st sector"),
            @Parameter(name = "category", description = "Category of the specialty", example = "Senior", schema = @Schema(implementation = SpecialtyCategory.class)),
            @Parameter(name = "createdAfter", description = "Date after which the specialty was created", example = "2025-01-01T00:00:00"),
            @Parameter(name = "createdBefore", description = "Date before which the specialty was created", example = "2025-01-01T00:00:00"),
            @Parameter(name = "updatedAfter", description = "Date after which the specialty was updated", example = "2025-01-01T00:00:00"),
            @Parameter(name = "updatedBefore", description = "Date before which the specialty was updated", example = "2025-01-01T00:00:00"),
            @Parameter(name = "pageNumber", description = "Page number", example = "0"),
            @Parameter(name = "pageSize", description = "Number of sectors per page", example = "10")
    })
    @GetMapping("/getAll")
    public Page<SpecialtyDto> getSpecialtiesByFilter
            (@RequestBody @Valid SpecialtyReceivingRequest specialtyReceivingRequest) {
        return specialtyApi.getAll(specialtyReceivingRequest);
    }
}