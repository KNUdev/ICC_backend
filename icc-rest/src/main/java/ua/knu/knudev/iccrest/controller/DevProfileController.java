package ua.knu.knudev.iccrest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.knu.knudev.applicationmanagerapi.devprofile.DevProfileApplicationManagerApi;
import ua.knu.knudev.applicationmanagerapi.dto.DepartmentDto;
import ua.knu.knudev.employeemanagerapi.devprofile.DevProfileEmployeeManagerApi;
import ua.knu.knudev.employeemanagerapi.dto.SectorDto;
import ua.knu.knudev.employeemanagerapi.dto.SpecialtyDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dev")
@Profile("dev")
public class DevProfileController {

    private final DevProfileEmployeeManagerApi devProfileEmployeeManagerApi;
    private final DevProfileApplicationManagerApi devProfileApplicationManagerApi;

    @Operation(summary = "Create test sectors")
    @PostMapping("/sectors/create/{numberOfSectors}")
    public List<SectorDto> createTestSectors(@PathVariable @Parameter(
            name = "Number of sectors to create",
            in = ParameterIn.PATH,
            required = true
    ) int numberOfSectors) {
        return devProfileEmployeeManagerApi.createTestSectors(numberOfSectors);
    }

    @Operation(summary = "Create test specialties",
            description = "Creates new specialties and sector for them")
    @PostMapping("/specialties/create/{numberOfSpecialties}")
    public List<SpecialtyDto> createTestSpecialties(@PathVariable @Parameter(
            name = "Number of specialties to create",
            in = ParameterIn.PATH,
            required = true
    ) int numberOfSpecialties) {
        return devProfileEmployeeManagerApi.createTestSpecialties(numberOfSpecialties);
    }

    @Operation(summary = "Create test departments")
    @PostMapping("/departments/create/{numberOfDepartments}")
    public List<DepartmentDto> createTestDepartments(@PathVariable("numberOfDepartments") @Parameter(
            name = "Number of departments to create",
            in = ParameterIn.PATH,
            required = true
    ) int numberOfDepartments) {
        return devProfileApplicationManagerApi.createTestDepartments(numberOfDepartments);
    }
}
