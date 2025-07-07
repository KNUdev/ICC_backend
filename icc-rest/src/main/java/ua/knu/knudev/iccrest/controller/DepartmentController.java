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
import ua.knu.knudev.iccsecurityapi.response.ErrorResponse;

import java.util.UUID;

@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentApi departmentApi;

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
    public Page<DepartmentDto> getAll(@RequestBody @Valid @Parameter(
            name = "Department receiving request",
            description = "Department filtering fields",
            in = ParameterIn.DEFAULT,
            required = true,
            schema = @Schema(implementation = DepartmentGetAllRequest.class)) DepartmentGetAllRequest request) {
        return departmentApi.getAll(request);
    }
}