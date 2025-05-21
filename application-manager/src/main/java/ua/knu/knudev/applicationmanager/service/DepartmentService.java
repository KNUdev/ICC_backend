package ua.knu.knudev.applicationmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ua.knu.knudev.applicationmanager.domain.Department;
import ua.knu.knudev.applicationmanager.mapper.DepartmentMapper;
import ua.knu.knudev.applicationmanager.mapper.MultiLanguageFieldMapper;
import ua.knu.knudev.applicationmanager.repository.DepartmentRepository;
import ua.knu.knudev.applicationmanagerapi.api.DepartmentApi;
import ua.knu.knudev.applicationmanagerapi.dto.DepartmentDto;
import ua.knu.knudev.applicationmanagerapi.request.DepartmentCreateRequest;
import ua.knu.knudev.applicationmanagerapi.request.DepartmentDeleteRequest;
import ua.knu.knudev.applicationmanagerapi.request.DepartmentGetAllRequest;
import ua.knu.knudev.applicationmanagerapi.request.DepartmentGetByIdRequest;
import ua.knu.knudev.applicationmanagerapi.request.DepartmentUpdateRequest;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class DepartmentService implements DepartmentApi {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final MultiLanguageFieldMapper multiLanguageFieldMapper;

    @Override
    public DepartmentDto create(DepartmentCreateRequest request) {
        Department department = departmentMapper.fromCreateRequest(request);
        department.setCreatedAt(LocalDateTime.now());
        department.setUpdatedAt(LocalDateTime.now());

        department = departmentRepository.save(department);
        return departmentMapper.toDto(department);
    }

    @Override
    public DepartmentDto update(DepartmentUpdateRequest request) {
        Department department = departmentRepository.findById(request.id())
                .orElseThrow(() -> new IllegalArgumentException("Department with ID: " + request.id() + " not found!"));

        department.setName(getOrDefault(request.name(), department.getName(), multiLanguageFieldMapper::toDomain));

        department.setUpdatedAt(LocalDateTime.now());

        department = departmentRepository.save(department);
        return departmentMapper.toDto(department);
    }

    @Override
    public DepartmentDto getById(DepartmentGetByIdRequest request) {
        Department department = departmentRepository.findById(request.id())
                .orElseThrow(() -> new IllegalArgumentException("Department with ID: " + request.id() + " not found!"));
        return departmentMapper.toDto(department);
    }

    @Override
    public Page<DepartmentDto> getAll(DepartmentGetAllRequest request) {
        return departmentRepository.findAll(PageRequest.of(request.page(), request.size()))
                .map(departmentMapper::toDto);
    }

    @Override
    public DepartmentDto delete(DepartmentDeleteRequest request) {
        Department department = departmentRepository.findById(request.id())
                .orElseThrow(() -> new IllegalArgumentException("Department with ID: " + request.id() + " not found!"));

        departmentRepository.delete(department);
        return departmentMapper.toDto(department);
    }

    private <T> T getOrDefault(T newValue, T currentValue) {
        return newValue != null ? newValue : currentValue;
    }

    private <T, R> R getOrDefault(T newValue, R currentValue, Function<T, R> mapper) {
        return newValue != null ? Objects.requireNonNullElse(mapper.apply(newValue), currentValue) : currentValue;
    }

    private <T, R> R mapIfNull(R currentValue, T newValue, Function<T, R> mapper) {
        return (currentValue == null && newValue != null) ? mapper.apply(newValue) : currentValue;
    }
}
