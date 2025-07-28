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
import ua.knu.knudev.applicationmanagerapi.request.ApplicationAddAssignedEmployeeRequest;
import ua.knu.knudev.applicationmanagerapi.request.ApplicationGetAllRequest;
import ua.knu.knudev.applicationmanagerapi.request.ApplicationRemoveAssignedEmployeeRequest;
import ua.knu.knudev.applicationmanagerapi.request.ApplicationUpdateRequest;
import ua.knu.knudev.iccsecurityapi.response.ErrorResponse;

import java.util.UUID;

@RestController
@RequestMapping("/admin/application")
@RequiredArgsConstructor
public class AdminApplicationController {

    private final ApplicationApi applicationApi;

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
                    in = ParameterIn.DEFAULT,
                    schema = @Schema(implementation = ApplicationUpdateRequest.class))
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
    public ApplicationDto addAssignedEmployee(@RequestBody @Valid @Parameter(
            name = "Add assigned employee request",
            description = "Adding employee to application",
            in = ParameterIn.DEFAULT,
            required = true,
            schema = @Schema(implementation = ApplicationAddAssignedEmployeeRequest.class))
                                              ApplicationAddAssignedEmployeeRequest request) {
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
    public ApplicationDto removeAssignedEmployee(@RequestBody @Valid @Parameter(
            name = "Remove assigned employee request",
            description = "Removing employee from application",
            in = ParameterIn.DEFAULT,
            required = true,
            schema = @Schema(implementation = ApplicationRemoveAssignedEmployeeRequest.class))
                                                 ApplicationRemoveAssignedEmployeeRequest request) {
        return applicationApi.removeAssignedEmployee(request);
    }
}