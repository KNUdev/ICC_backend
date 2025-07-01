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
import org.springframework.web.bind.annotation.*;
import ua.knu.knudev.applicationmanagerapi.api.DepartmentApi;
import ua.knu.knudev.applicationmanagerapi.dto.DepartmentDto;
import ua.knu.knudev.applicationmanagerapi.request.DepartmentGetAllRequest;
import ua.knu.knudev.applicationmanagerapi.request.DepartmentUpdateRequest;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;
import ua.knu.knudev.iccsecurityapi.response.ErrorResponse;

import java.util.UUID;

@RestController
@RequestMapping("/admin/department")
@RequiredArgsConstructor
public class AdminDepartmentController {
    private final DepartmentApi departmentApi;

    @Operation(summary = "Create a new department",
            description = "Creates a department")
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    description = "Department created successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DepartmentDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Invalid request",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public DepartmentDto create(@RequestBody @Valid MultiLanguageFieldDto departmentName) {
        return departmentApi.create(departmentName);
    }

    @Operation(summary = "Update an existing department",
            description = "Updates a department")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Department successfully updated",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DepartmentDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Department not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PatchMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public DepartmentDto update(@RequestBody @Valid DepartmentUpdateRequest request) {
        return departmentApi.update(request);
    }

    @Operation(summary = "Get department by ID",
            description = "Fetches detailed information about a department based on the provided department ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Department successfully retrieved",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DepartmentDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Department not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/{departmentId}")
    @ResponseStatus(HttpStatus.OK)
    public DepartmentDto getById(@PathVariable @Valid @Parameter(
            name = "Department UUID",
            description = "UUID department need to receive",
            in = ParameterIn.PATH,
            required = true,
            schema = @Schema(implementation = UUID.class)
    ) UUID departmentId) {
        return departmentApi.getById(departmentId);
    }

    @Operation(summary = "Get all departments with pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Departments successfully retrieved",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DepartmentDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Department not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<DepartmentDto> getAll(@RequestBody @Valid DepartmentGetAllRequest request) {
        return departmentApi.getAll(request);
    }

    @Operation(summary = "Delete a department")
    @DeleteMapping("/{departmentId}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID departmentId) {
        departmentApi.delete(departmentId);
    }
}