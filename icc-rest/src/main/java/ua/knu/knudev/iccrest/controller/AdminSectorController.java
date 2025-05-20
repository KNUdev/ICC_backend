package ua.knu.knudev.iccrest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import ua.knu.knudev.employeemanagerapi.api.SectorApi;
import ua.knu.knudev.employeemanagerapi.dto.SectorDto;
import ua.knu.knudev.employeemanagerapi.request.SectorCreationRequest;
import ua.knu.knudev.employeemanagerapi.request.SectorReceivingRequest;
import ua.knu.knudev.employeemanagerapi.request.SectorUpdateRequest;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/sector")
public class AdminSectorController {

    private final SectorApi sectorApi;

    @Operation(
            summary = "Create a new sector",
            description = "Creates a sector and returns it."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Sector successfully created.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SectorDto.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input provided.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public SectorDto create(
            @Valid @RequestBody @Parameter(
                    name = "Sector creation request",
                    description = "Sector details",
                    in = ParameterIn.QUERY,
                    required = true,
                    schema = @Schema(implementation = SectorCreationRequest.class)
            ) SectorCreationRequest request
    ) {
        return sectorApi.create(request);
    }

    @Operation(
            summary = "Update sector",
            description = "Allows to update a sector"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Sector successfully updated",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SectorDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sector is not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PatchMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public SectorDto update(
            @Valid @RequestBody @Parameter(
                    name = "Sector update request",
                    description = "Sector update data",
                    in = ParameterIn.QUERY,
                    required = true,
                    schema = @Schema(implementation = SectorUpdateRequest.class)
            ) SectorUpdateRequest request
    ) {
        return sectorApi.update(request);
    }

    @Operation(
            summary = "Retrieve sector",
            description = "Fetches detailed information about a sector based on the provided sector ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Sector was successfully retrieved",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SectorDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sector is not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/{sectorId}")
    @ResponseStatus(HttpStatus.OK)
    public SectorDto getById(@PathVariable UUID sectorId) {
        return sectorApi.getById(sectorId);
    }

    @Operation(
            summary = "Retrieve a page of sectors",
            description = "Fetches information about filtered sectors."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Sectors were successfully retrieved.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SectorDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sectors are not found.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<SectorDto> getAll(
            @RequestBody @Parameter(
                    name = "Sector receiving request",
                    description = "Sector filtering fields",
                    in = ParameterIn.QUERY,
                    required = true,
                    schema = @Schema(implementation = SectorReceivingRequest.class)
            ) SectorReceivingRequest request
    ) {
        return sectorApi.getAll(request);
    }

    @DeleteMapping("/{sectorId}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID sectorId) {
        sectorApi.delete(sectorId);
    }
}