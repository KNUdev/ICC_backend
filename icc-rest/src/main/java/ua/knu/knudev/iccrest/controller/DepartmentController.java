package ua.knu.knudev.iccrest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ua.knu.knudev.applicationmanagerapi.api.DepartmentApi;
import ua.knu.knudev.applicationmanagerapi.dto.DepartmentDto;
import ua.knu.knudev.applicationmanagerapi.request.*;

@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentApi departmentApi;

    @Operation(summary = "Create a new department")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Department created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping("/create")
    public DepartmentDto create(@RequestBody @Valid DepartmentCreateRequest request) {
        return departmentApi.create(request);
    }

    @Operation(summary = "Update an existing department")
    @PatchMapping("/update")
    public DepartmentDto update(@RequestBody @Valid DepartmentUpdateRequest request) {
        return departmentApi.update(request);
    }

    @Operation(summary = "Get department by ID")
    @GetMapping("/get-by-id")
    public DepartmentDto getById(@RequestBody @Valid DepartmentGetByIdRequest request) {
        return departmentApi.getById(request);
    }

    @Operation(summary = "Get all departments with pagination")
    @GetMapping("/get-all")
    public Page<DepartmentDto> getAll(@RequestBody @Valid DepartmentGetAllRequest request) {
        return departmentApi.getAll(request);
    }

    @Operation(summary = "Delete a department")
    @DeleteMapping("/delete")
    public DepartmentDto delete(@RequestBody @Valid DepartmentDeleteRequest request) {
        return departmentApi.delete(request);
    }
}