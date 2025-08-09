package ua.knu.knudev.applicationmanagerapi.devprofile;

import ua.knu.knudev.applicationmanagerapi.dto.DepartmentDto;

import java.util.List;

public interface DevProfileApplicationManagerApi {

    List<DepartmentDto> createTestDepartments(int numberOfDepartments);

}
