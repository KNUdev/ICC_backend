package ua.knu.knudev.iccrest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import ua.knu.knudev.applicationmanagerapi.request.*;
import ua.knu.knudev.iccsecurityapi.response.ErrorResponse;

import java.util.UUID;

@RestController
@RequestMapping("/admin/application")
@RequiredArgsConstructor
public class AdminApplicationController {
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
                    content = @Content(schema = @Schema(implementation = ApplicationCreateRequest.class)))
            @RequestBody @Valid ApplicationCreateRequest request) {
        return applicationApi.create(request);
    }

    @Operation(summary = "Update an existing application",
            description = "Updates an existing application with the provided data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Application successfully updated",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApplicationDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    @PatchMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public ApplicationDto update(
            @Parameter(description = "Application update request body",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ApplicationUpdateRequest.class)))
            @RequestBody @Valid ApplicationUpdateRequest request) {
        return applicationApi.update(request);
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

    @Operation(summary = "Delete application by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Application deleted",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApplicationDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Application not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{applicationId}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Valid UUID applicationId) {
        applicationApi.delete(applicationId);
    }

    @Operation(summary = "Add assigned employee to application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Employee added",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApplicationDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Application not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/assign-employee")
    public ApplicationDto addAssignedEmployee(@RequestBody @Valid ApplicationAddAssignedEmployeeRequest request) {
        return applicationApi.addAssignedEmployee(request);
    }

    @Operation(summary = "Remove assigned employee from application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Employee removed",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApplicationDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Application not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/remove-employee")
    public ApplicationDto removeAssignedEmployee(@RequestBody @Valid ApplicationRemoveAssignedEmployeeRequest request) {
        return applicationApi.removeAssignedEmployee(request);
    }
}