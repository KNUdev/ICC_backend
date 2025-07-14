package ua.knu.knudev.iccrest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import ua.knu.knudev.employeemanagerapi.api.SpecialtyApi;
import ua.knu.knudev.employeemanagerapi.dto.SpecialtyDto;
import ua.knu.knudev.employeemanagerapi.request.SpecialtyReceivingRequest;
import ua.knu.knudev.icccommon.constant.SpecialtyCategory;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/specialty")
public class SpecialtyController {

    private final SpecialtyApi specialtyApi;

    @Operation(
            summary = "Retrieve specialty",
            description = "Fetches detailed information about a specialty based on the provided specialty ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Specialty was successfully retrieved",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SpecialtyDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Specialty is not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/{specialtyId}")
    public SpecialtyDto getSpecialtyById(@PathVariable UUID specialtyId) {
        return specialtyApi.getById(specialtyId);
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Specialties were successfully retrieved.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SpecialtyDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Specialties are not found.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))
            )
    })
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
    @PostMapping("/getAll")
    public Page<SpecialtyDto> getSpecialtiesByFilter
            (@RequestBody @Valid SpecialtyReceivingRequest specialtyReceivingRequest) {
        return specialtyApi.getAll(specialtyReceivingRequest);
    }
}