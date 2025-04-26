package ua.knu.knudev.applicationmanager.mapper;

import ua.knu.knudev.applicationmanager.domain.Application;
import ua.knu.knudev.applicationmanager.domain.Department;
import ua.knu.knudev.applicationmanager.dto.ApplicationDto;

public class ApplicationMapper {

    public static Application toEntity(ApplicationDto dto, Department department) {
        return Application.builder()
                .id(dto.getId())
                .applicantName(dto.getApplicantName())
                .email(dto.getEmail())
                .receivedAt(dto.getReceivedAt())
                .completedAt(dto.getCompletedAt())
                .problemDescription(dto.getProblemDescription())
                .problemPhoto(dto.getProblemPhoto())
                .status(dto.getStatus())
                .Department(department)
                .assignedEmployeeIds(dto.getAssignedEmployeeIds())
                .build();
    }

    public static ApplicationDto toDto(Application application) {
        return ApplicationDto.builder()
                .id(application.getId())
                .applicantName(application.getApplicantName())
                .email(application.getEmail())
                .receivedAt(application.getReceivedAt())
                .completedAt(application.getCompletedAt())
                .problemDescription(application.getProblemDescription())
                .problemPhoto(application.getProblemPhoto())
                .status(application.getStatus())
                .departmentId(application.getDepartment() != null ? application.getDepartment().getId() : null)
                .assignedEmployeeIds(application.getAssignedEmployeeIds())
                .build();
    }
}
