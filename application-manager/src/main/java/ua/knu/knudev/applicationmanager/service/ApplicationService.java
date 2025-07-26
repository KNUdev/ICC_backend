package ua.knu.knudev.applicationmanager.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.knu.knudev.applicationmanager.domain.Application;
import ua.knu.knudev.applicationmanager.domain.Department;
import ua.knu.knudev.applicationmanager.mapper.ApplicationMapper;
import ua.knu.knudev.applicationmanager.repository.ApplicationRepository;
import ua.knu.knudev.applicationmanager.repository.DepartmentRepository;
import ua.knu.knudev.applicationmanagerapi.api.ApplicationApi;
import ua.knu.knudev.applicationmanagerapi.dto.ApplicationDto;
import ua.knu.knudev.applicationmanagerapi.exception.ApplicationException;
import ua.knu.knudev.applicationmanagerapi.exception.DepartmentException;
import ua.knu.knudev.applicationmanagerapi.request.*;
import ua.knu.knudev.employeemanagerapi.api.EmployeeApi;
import ua.knu.knudev.fileserviceapi.api.ImageServiceApi;
import ua.knu.knudev.fileserviceapi.subfolder.ImageSubfolder;
import ua.knu.knudev.icccommon.domain.embeddable.FullName;
import ua.knu.knudev.icccommon.mapper.FullNameMapper;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService implements ApplicationApi {

    private final ApplicationMapper applicationMapper;
    private final ApplicationRepository applicationRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeApi employeeApi;
    private final ImageServiceApi imageServiceApi;
    private final FullNameMapper fullNameMapper;

    @Override
    @Transactional
    public ApplicationDto create(ApplicationCreateRequest request) {
        Department department = getDepartmentById(request.departmentId());

        String filename = request.problemPhoto().getOriginalFilename();
        String uploadedProblemPhoto = uploadProblemPhoto(request.problemPhoto(), filename,
                ImageSubfolder.APPLICATIONS);

        FullName applicantName = fullNameMapper.toDomain(request.applicantName());

        Application application = Application.builder()
                .applicantName(applicantName)
                .applicantEmail(request.applicantEmail())
                .isPrivate(false)
                .receivedAt(LocalDateTime.now())
                .problemDescription(request.problemDescription())
                .problemPhoto(uploadedProblemPhoto)
                .status(request.status())
                .department(department)
                .assignedEmployeeIds(new HashSet<>())
                .build();

        department.setApplication(application);

        departmentRepository.save(department);
        application = applicationRepository.save(application);

        return buildApplicationDto(application);
    }

    @Override
    public ApplicationDto createPrivateApplication(PrivateApplicationCreateRequest request) {
        String filename = request.problemPhoto().getOriginalFilename();
        String uploadedProblemPhoto = uploadProblemPhoto(request.problemPhoto(), filename, ImageSubfolder.APPLICATIONS);

        FullName applicantName = fullNameMapper.toDomain(request.applicantName());

        Application application = Application.builder()
                .applicantName(applicantName)
                .applicantEmail(request.applicantEmail())
                .isPrivate(true)
                .receivedAt(LocalDateTime.now())
                .problemDescription(request.problemDescription())
                .problemPhoto(uploadedProblemPhoto)
                .status(request.status())
                .assignedEmployeeIds(new HashSet<>())
                .build();

        application = applicationRepository.save(application);

        return buildApplicationDto(application);
    }

    @Override
    public ApplicationDto update(ApplicationUpdateRequest request) {
        Application application = getApplicationById(request.id());

        if (request.departmentId() != null) {
            Department department = getDepartmentById(request.departmentId());
            application.setDepartment(department);
        }

        application.setApplicantName(getOrDefault(request.applicantName(), application.getApplicantName(), fullNameMapper::toDomain));
        application.setApplicantEmail(getOrDefault(request.applicantEmail(), application.getApplicantEmail()));
        application.setCompletedAt(getOrDefault(request.completedAt(), application.getCompletedAt()));
        application.setProblemDescription(getOrDefault(request.problemDescription(), application.getProblemDescription()));
        application.setStatus(getOrDefault(request.status(), application.getStatus()));

        application = applicationRepository.save(application);
        return buildApplicationDto(application);
    }

    @Override
    @Transactional
    public ApplicationDto getById(UUID applicationId) {
        Application application = getApplicationById(applicationId);

        return buildApplicationDto(application);
    }

    @Override
    public List<ApplicationDto> getByAssignedEmployeeId(UUID employeeId) {
        List<Application> applications = applicationRepository.findApplicationsByAssignedEmployeeIds(employeeId)
                .orElseThrow(() -> new ApplicationException("Applications assigned to employee with id " + employeeId + " not found"));

        return applications.stream().map(this::buildApplicationDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Page<ApplicationDto> getAll(ApplicationGetAllRequest request) {
        int pageNumber = getOrDefault(request.pageNumber(), 0);
        int pageSize = getOrDefault(request.pageSize(), 10);
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        Page<Application> applications = applicationRepository.findAllBySearchQuery(paging, request);

        return applications.map(this::buildApplicationDto);
    }

    @Override
    @Transactional
    public void delete(UUID applicationId) {
        if (!applicationRepository.existsById(applicationId)) {
            throw new ApplicationException("Application with ID: " + applicationId + " not found!");
        }
        applicationRepository.deleteById(applicationId);
    }

    @Override
    public ApplicationDto addAssignedEmployee(ApplicationAddAssignedEmployeeRequest request) {
        Application application = getApplicationById(request.applicationId());

        if (!employeeApi.existsById(request.employeeId())) {
            throw new ApplicationException("Employee with ID: " + request.employeeId() + " not found!");
        }

        application.addAssignedEmployee(request.employeeId());
        application = applicationRepository.save(application);
        return buildApplicationDto(application);
    }

    @Override
    public ApplicationDto removeAssignedEmployee(ApplicationRemoveAssignedEmployeeRequest request) {
        Application application = getApplicationById(request.applicationId());

        if (!employeeApi.existsById(request.employeeId())) {
            throw new ApplicationException("Employee with ID: " + request.employeeId() + " not found!");
        }

        application.getAssignedEmployeeIds().remove(request.employeeId());
        application = applicationRepository.save(application);
        return buildApplicationDto(application);
    }

    private ApplicationDto buildApplicationDto(Application application) {
        String imagePath = imageServiceApi.getPathByFilename(application.getProblemPhoto(), ImageSubfolder.APPLICATIONS);

        return ApplicationDto.builder()
                .id(application.getId())
                .applicantName(application.getApplicantName())
                .applicantEmail(application.getApplicantEmail())
                .receivedAt(application.getReceivedAt())
                .completedAt(application.getCompletedAt())
                .problemDescription(application.getProblemDescription())
                .problemPhoto(imagePath)
                .status(application.getStatus())
                .departmentId(application.getDepartment().getId())
                .assignedEmployeeIds(application.getAssignedEmployeeIds())
                .build();
    }

    private String uploadProblemPhoto(MultipartFile problemPhoto, String imageName, ImageSubfolder subfolder) {
        if (ObjectUtils.isEmpty(problemPhoto)) {
            return null;
        }
        return imageServiceApi.uploadFile(problemPhoto, imageName, subfolder);
    }

    private Application getApplicationById(UUID applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationException("Application with ID: " + applicationId + " not found!"));
    }

    private Department getDepartmentById(UUID departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentException("Department with ID: " + departmentId + " not found!"));
    }

    private <T> T getOrDefault(T newValue, T currentValue) {
        return newValue != null ? newValue : currentValue;
    }

    private <T, R> R getOrDefault(T newValue, R currentValue, Function<T, R> mapper) {
        return newValue != null ? Objects.requireNonNullElse(mapper.apply(newValue), currentValue) : currentValue;
    }
}
