package ua.knu.knudev.applicationmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ua.knu.knudev.applicationmanager.domain.Application;
import ua.knu.knudev.applicationmanager.domain.Department;
import ua.knu.knudev.applicationmanager.mapper.ApplicationMapper;
import ua.knu.knudev.applicationmanager.repository.ApplicationRepository;
import ua.knu.knudev.applicationmanager.repository.DepartmentRepository;
import ua.knu.knudev.applicationmanagerapi.api.ApplicationApi;
import ua.knu.knudev.applicationmanagerapi.dto.ApplicationDto;
import ua.knu.knudev.applicationmanagerapi.exception.ApplicationException;
import ua.knu.knudev.applicationmanagerapi.request.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ApplicationService implements ApplicationApi {

    private final ApplicationMapper applicationMapper;
    private final ApplicationRepository applicationRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public ApplicationDto create(ApplicationCreateRequest request) {
        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));

        Application application = Application.builder()
                .applicantName(request.applicantName())
                .email(request.email())
                .assignedEmployeeIds(request.assignedEmployeeIds())
                .receivedAt(LocalDateTime.now())
                .problemDescription(request.problemDescription())
                .problemPhoto(request.problemPhoto())
                .status(request.status())
                .department(department)
                .build();

        application = applicationRepository.save(application);

        return applicationMapper.toDto(application);
    }

    @Override
    public ApplicationDto update(ApplicationUpdateRequest request) {
        Application application = applicationRepository.findById(request.id()).orElseThrow(
                () -> new ApplicationException("Application with ID: " + request.id() + " not found!")
        );

        if (request.departmentId() != null) {
            Department department = departmentRepository.findById(request.departmentId())
                    .orElseThrow(() -> new IllegalArgumentException("Department not found"));
            application.setDepartment(department);
        }

        application.setApplicantName(getOrDefault(request.applicantName(), application.getApplicantName()));
        application.setEmail(getOrDefault(request.email(), application.getEmail()));
        application.setCompletedAt(getOrDefault(request.completedAt(), application.getCompletedAt()));
        application.setProblemDescription(getOrDefault(request.problemDescription(),
                application.getProblemDescription()));
        application.setProblemPhoto(getOrDefault(request.problemPhoto(), application.getProblemPhoto()));
        application.setStatus(getOrDefault(request.status(), application.getStatus()));

        application = applicationRepository.save(application);
        return applicationMapper.toDto(application);
    }

    @Override
    public ApplicationDto getById(ApplicationGetByIdRequest request) {
        Application application = applicationRepository.findById(request.id())
                .orElseThrow(() -> new ApplicationException("Application with ID: " + request.id() + " not found!"));
        return applicationMapper.toDto(application);
    }

    @Override
    public Page<ApplicationDto> getAll(ApplicationGetAllRequest request) {
        PageRequest pageRequest = PageRequest.of(request.page(), request.size());
        return applicationRepository.findAll(pageRequest)
                .map(applicationMapper::toDto);
    }

    @Override
    public ApplicationDto delete(ApplicationDeleteRequest request) {
        Application application = applicationRepository.findById(request.id())
                .orElseThrow(() -> new ApplicationException("Application with ID: " + request.id() + " not found!"));

        applicationRepository.delete(application);
        return applicationMapper.toDto(application);
    }

    @Override
    public ApplicationDto addAssignedEmployee(ApplicationAddAssignedEmployeeRequest request) {
        Application application = applicationRepository.findById(request.applicationId())
                .orElseThrow(() -> new ApplicationException("Application with ID: " +
                        request.applicationId() + " not found!"));

        application.getAssignedEmployeeIds().add(request.employeeId());
        application = applicationRepository.save(application);
        return applicationMapper.toDto(application);
    }

    @Override
    public ApplicationDto removeAssignedEmployee(ApplicationRemoveAssignedEmployeeRequest request) {
        Application application = applicationRepository.findById(request.applicationId())
                .orElseThrow(() -> new ApplicationException("Application with ID: " +
                        request.applicationId() + " not found!"));

        application.getAssignedEmployeeIds().remove(request.employeeId());
        application = applicationRepository.save(application);
        return applicationMapper.toDto(application);
    }

    private <T> T getOrDefault(T newValue, T currentValue) {
        return newValue != null ? newValue : currentValue;
    }

    private <T, R> R getOrDefault(T newValue, R currentValue, Function<T, R> mapper) {
        return newValue != null ? Objects.requireNonNullElse(mapper.apply(newValue), currentValue) :
                currentValue;
    }

    private <T, R> R mapIfNull(R currentValue, T newValue, Function<T, R> mapper) {
        return (currentValue == null && newValue != null) ? mapper.apply(newValue) : currentValue;
    }
}
