package ua.knu.knudev.iccrest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.knu.knudev.applicationmanagerapi.api.ApplicationApi;
import ua.knu.knudev.applicationmanagerapi.dto.ApplicationDto;
import ua.knu.knudev.applicationmanagerapi.request.ApplicationGetAllRequest;
import ua.knu.knudev.iccsecurityapi.response.ErrorResponse;

import java.util.UUID;

@RestController
@RequestMapping("/application")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationApi applicationApi;

    @Operation(summary = "Get application by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Application found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApplicationDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Application not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{applicationId}")
    @ResponseStatus(HttpStatus.OK)
    public ApplicationDto getById(@PathVariable @Valid UUID applicationId) {
        return applicationApi.getById(applicationId);
    }

    @Operation(summary = "Get all applications")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "List of applications",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApplicationDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Application not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<ApplicationDto> getAll(@RequestBody @Valid ApplicationGetAllRequest request) {
        return applicationApi.getAll(request);
    }
}