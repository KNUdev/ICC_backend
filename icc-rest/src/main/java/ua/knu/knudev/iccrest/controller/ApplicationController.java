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
import org.springframework.web.bind.annotation.*;
import ua.knu.knudev.applicationmanagerapi.api.ApplicationApi;
import ua.knu.knudev.applicationmanagerapi.dto.ApplicationDto;
import ua.knu.knudev.applicationmanagerapi.request.*;

@RestController
@RequestMapping("/application")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationApi applicationApi;

    @Operation(summary = "Create a new application", description = "Creates a new application using the provided request data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application successfully created", content = @Content(schema = @Schema(implementation = ApplicationDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    @PostMapping("/create")
    public ApplicationDto create(
            @Parameter(description = "Application creation request body", required = true, in = ParameterIn.QUERY, content = @Content(schema = @Schema(implementation = ApplicationCreateRequest.class)))
            @RequestBody @Valid ApplicationCreateRequest request) {
        return applicationApi.create(request);
    }

    @Operation(summary = "Update an existing application", description = "Updates an existing application with the provided data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application successfully updated", content = @Content(schema = @Schema(implementation = ApplicationDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    @PatchMapping("/update")
    public ApplicationDto update(
            @Parameter(description = "Application update request body", required = true, in = ParameterIn.QUERY, content = @Content(schema = @Schema(implementation = ApplicationUpdateRequest.class)))
            @RequestBody @Valid ApplicationUpdateRequest request) {
        return applicationApi.update(request);
    }

    @Operation(summary = "Get application by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application found", content = @Content(schema = @Schema(implementation = ApplicationDto.class))),
            @ApiResponse(responseCode = "404", description = "Application not found")
    })
    @GetMapping("/get-by-id")
    public ApplicationDto getById(@RequestBody @Valid ApplicationGetByIdRequest request) {
        return applicationApi.getById(request);
    }

    @Operation(summary = "Get all applications")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of applications", content = @Content(schema = @Schema(implementation = ApplicationDto.class)))
    })
    @GetMapping("/get-all")
    public Page<ApplicationDto> getAll(@RequestBody @Valid ApplicationGetAllRequest request) {
        return applicationApi.getAll(request);
    }

    @Operation(summary = "Delete application by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application deleted", content = @Content(schema = @Schema(implementation = ApplicationDto.class))),
            @ApiResponse(responseCode = "404", description = "Application not found")
    })
    @DeleteMapping("/delete")
    public ApplicationDto delete(@RequestBody @Valid ApplicationDeleteRequest request) {
        return applicationApi.delete(request);
    }

    @Operation(summary = "Add assigned employee to application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee added", content = @Content(schema = @Schema(implementation = ApplicationDto.class))),
            @ApiResponse(responseCode = "404", description = "Application not found")
    })
    @PostMapping("/assign-employee")
    public ApplicationDto addAssignedEmployee(@RequestBody @Valid ApplicationAddAssignedEmployeeRequest request) {
        return applicationApi.addAssignedEmployee(request);
    }

    @Operation(summary = "Remove assigned employee from application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee removed", content = @Content(schema = @Schema(implementation = ApplicationDto.class))),
            @ApiResponse(responseCode = "404", description = "Application not found")
    })
    @PostMapping("/remove-employee")
    public ApplicationDto removeAssignedEmployee(@RequestBody @Valid ApplicationRemoveAssignedEmployeeRequest request) {
        return applicationApi.removeAssignedEmployee(request);
    }
}
