package ua.knu.knudev.reportmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.knu.knudev.employeemanagerapi.api.EmployeeApi;
import ua.knu.knudev.employeemanagerapi.response.GetEmployeeResponse;
import ua.knu.knudev.reportmanagerapi.dto.ReportRowDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DataFetchService {

    private final EmployeeApi employeeApi;

    protected List<ReportRowDto> getReportRowDto() {
        Set<GetEmployeeResponse> employeeResponses = employeeApi.getAll();

        return employeeResponses.stream().map(response -> {
            String peopleName = response.name().getLastName() + " " +
                    response.name().getFirstName() + " " +
                    response.name().getMiddleName();

            String position = response.specialty().name().getEn() +
                    ", " + response.sector().name().getEn();
            return ReportRowDto.builder()
                    .id(response.id())
                    .name(peopleName)
                    .email(response.email())
                    .phoneNumber(response.phoneNumber())
                    .position(position)
                    .salary(response.salaryInUAH())
                    .contractValidTo(response.contractEndDate())
                    .build();
        }).collect(Collectors.toList());
    }
}
