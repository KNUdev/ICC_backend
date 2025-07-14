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
import org.springframework.web.multipart.MultipartFile;
import ua.knu.knudev.employeemanagerapi.api.EmployeeApi;
import ua.knu.knudev.employeemanagerapi.dto.EmployeeDto;
import ua.knu.knudev.employeemanagerapi.request.EmployeeCreationRequest;
import ua.knu.knudev.employeemanagerapi.request.EmployeeReceivingRequest;
import ua.knu.knudev.employeemanagerapi.request.EmployeeUpdateRequest;
import ua.knu.knudev.employeemanagerapi.response.GetEmployeeResponse;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/employee")
public class AdminEmployeeController {

    private final EmployeeApi employeeApi;

    @Operation(
            summary = "Create a new employee",
            description = "Creates an employee"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Employee successfully created.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EmployeeDto.class)
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
    public EmployeeDto create(
            @Valid @RequestBody @Parameter(
                    name = "Employee creation request",
                    description = "Employee details",
                    in = ParameterIn.QUERY,
                    required = true,
                    schema = @Schema(implementation = EmployeeCreationRequest.class)
            ) EmployeeCreationRequest request
    ) {
        return employeeApi.create(request);
    }

    @Operation(
            summary = "Update employee",
            description = "Allows to update an employee"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Employee successfully updated",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EmployeeDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Employee is not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PatchMapping(value = "/update")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeDto update(
            @Valid @RequestBody @Parameter(
                    name = "Employee update request",
                    description = "Employee update data",
                    in = ParameterIn.QUERY,
                    required = true,
                    schema = @Schema(implementation = EmployeeUpdateRequest.class)
            ) EmployeeUpdateRequest request
    ) {
        return employeeApi.update(request);
    }

    @Operation(
            summary = "Retrieve employee",
            description = "Fetches detailed information about an employee based on the provided employee ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Employee was successfully retrieved",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GetEmployeeResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Employee is not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public GetEmployeeResponse getById(@PathVariable UUID employeeId) {
        return employeeApi.getById(employeeId);
    }

    @Operation(
            summary = "Retrieve a page of employees",
            description = "Fetches information about filtered employees."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Employees were successfully retrieved.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GetEmployeeResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Employees are not found.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<GetEmployeeResponse> getAll(
            @RequestBody @Parameter(
                    name = "Employee receiving request",
                    description = "Sector filtering fields",
                    in = ParameterIn.QUERY,
                    required = true,
                    schema = @Schema(implementation = EmployeeReceivingRequest.class)
            ) EmployeeReceivingRequest request
    ) {
        return employeeApi.getAll(request);
    }

    @DeleteMapping("/{employeeId}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID employeeId) {
        employeeApi.deleteById(employeeId);
    }

    @PatchMapping(value = "/{employeeId}/avatar/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String updateAvatar(@PathVariable UUID employeeId, @RequestParam("newAvatar") MultipartFile newAvatar) {
        return employeeApi.updateAvatar(employeeId, newAvatar);
    }

    @DeleteMapping(value = "/{employeeId}/avatar/remove")
    public void deleteAvatar(@PathVariable UUID employeeId) {
        employeeApi.removeAvatar(employeeId);
    }
}