package ua.knu.knudev.applicationmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.knu.knudev.applicationmanager.domain.Department;
import ua.knu.knudev.applicationmanager.dto.DepartmentDto;
import ua.knu.knudev.applicationmanager.mapper.DepartmentMapper;
import ua.knu.knudev.applicationmanager.repository.DepartmentRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentDto create(DepartmentDto dto) {
        Department department = DepartmentMapper.toEntity(dto);
        department.setCreatedAt(LocalDateTime.now());
        department.setUpdatedAt(LocalDateTime.now());
        department = departmentRepository.save(department);
        return DepartmentMapper.toDto(department);
    }

    public DepartmentDto getById(UUID id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));
        return DepartmentMapper.toDto(department);
    }

    public DepartmentDto update(UUID id, DepartmentDto dto) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));

        department.getName().setEn(dto.getNameEn());
        department.getName().setUk(dto.getNameUk());
        department.setUpdatedAt(LocalDateTime.now());

        departmentRepository.save(department);
        return DepartmentMapper.toDto(department);
    }
}