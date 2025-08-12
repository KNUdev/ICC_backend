package ua.knu.knudev.employeemanagerapi.devprofile;

import ua.knu.knudev.employeemanagerapi.dto.SectorDto;
import ua.knu.knudev.employeemanagerapi.dto.SpecialtyDto;

import java.util.List;

public interface DevProfileEmployeeManagerApi {

    List<SectorDto> createTestSectors(int numberOfSectors);

    List<SpecialtyDto> createTestSpecialties(int numberOfSpecialties);

}