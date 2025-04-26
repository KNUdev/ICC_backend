package ua.knu.knudev.applicationmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.knu.knudev.applicationmanager.domain.Application;
import ua.knu.knudev.applicationmanager.domain.Department;
import ua.knu.knudev.applicationmanager.dto.ApplicationDto;
import ua.knu.knudev.applicationmanager.mapper.ApplicationMapper;
import ua.knu.knudev.applicationmanager.repository.ApplicationRepository;
import ua.knu.knudev.applicationmanager.repository.DepartmentRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final DepartmentRepository departmentRepository;

    public ApplicationDto create(ApplicationDto dto) {
        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));

        Application application = ApplicationMapper.toEntity(dto, department);
        application = applicationRepository.save(application);

        return ApplicationMapper.toDto(application);
    }

    public ApplicationDto getById(UUID id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));
        return ApplicationMapper.toDto(application);
    }

    public ApplicationDto update(UUID id, ApplicationDto dto) {
        Application existingApplication = applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        existingApplication.setApplicantName(dto.getApplicantName());
        existingApplication.setProblemDescription(dto.getProblemDescription());
        existingApplication.setProblemPhoto(dto.getProblemPhoto());
        existingApplication.setStatus(dto.getStatus());
        existingApplication.setCompletedAt(dto.getCompletedAt());
        existingApplication.setAssignedEmployeeIds(dto.getAssignedEmployeeIds());

        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new IllegalArgumentException("Department not found"));
            existingApplication.setDepartment(department);
        }

        applicationRepository.save(existingApplication);

        return ApplicationMapper.toDto(existingApplication);
    }
}
