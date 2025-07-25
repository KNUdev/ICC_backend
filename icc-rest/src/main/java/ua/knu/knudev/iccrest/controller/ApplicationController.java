package ua.knu.knudev.iccrest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
import ua.knu.knudev.applicationmanagerapi.request.ApplicationCreateRequest;
import ua.knu.knudev.applicationmanagerapi.request.ApplicationGetAllRequest;
import ua.knu.knudev.applicationmanagerapi.request.PrivateApplicationCreateRequest;
import ua.knu.knudev.iccsecurityapi.response.ErrorResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationApi applicationApi;

    @Operation(summary = "Create a new application",
            description = "Creates a new application using the provided request data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Application successfully created",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApplicationDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationDto create(
            @Parameter(description = "Application creation request body",
                    required = true,
                    in = ParameterIn.DEFAULT,
                    schema = @Schema(implementation = ApplicationCreateRequest.class))
            @RequestBody @Valid ApplicationCreateRequest request) {
        return applicationApi.create(request);
    }

    @Operation(summary = "Create a new private application",
            description = "Creates a new private application using the provided request data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Private application successfully created",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApplicationDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/private/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationDto createPrivate(
            @Parameter(description = "Private application creation request body",
                    required = true,
                    in = ParameterIn.DEFAULT,
                    schema = @Schema(implementation = ApplicationCreateRequest.class))
            @RequestBody @Valid PrivateApplicationCreateRequest request) {
        return applicationApi.createPrivateApplication(request);
    }

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

    @Operation(summary = "Get applications by assigned employee id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Application found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApplicationDto.class))),
    })
    @GetMapping("/assigned-employee/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ApplicationDto> getByAssignedEmployeeId(@PathVariable @Valid UUID employeeId) {
        return applicationApi.getByAssignedEmployeeId(employeeId);
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
    @PostMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<ApplicationDto> getAll(@RequestBody @Valid @Parameter(
            name = "Application receiving request",
            description = "Application filtering fields",
            in = ParameterIn.DEFAULT,
            required = true,
            schema = @Schema(implementation = ApplicationGetAllRequest.class)
    ) ApplicationGetAllRequest request) {
        return applicationApi.getAll(request);
    }
}