package ua.knu.knudev.applicationmanager.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.knu.knudev.applicationmanager.domain.Department;
import ua.knu.knudev.applicationmanager.mapper.DepartmentMapper;
import ua.knu.knudev.applicationmanager.repository.DepartmentRepository;
import ua.knu.knudev.applicationmanagerapi.api.DepartmentApi;
import ua.knu.knudev.applicationmanagerapi.dto.DepartmentDto;
import ua.knu.knudev.applicationmanagerapi.exception.DepartmentException;
import ua.knu.knudev.applicationmanagerapi.request.DepartmentGetAllRequest;
import ua.knu.knudev.applicationmanagerapi.request.DepartmentUpdateRequest;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;
import ua.knu.knudev.icccommon.mapper.MultiLanguageFieldMapper;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class DepartmentService implements DepartmentApi {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final MultiLanguageFieldMapper multiLanguageFieldMapper;

    @Override
    @Transactional
    public DepartmentDto create(MultiLanguageFieldDto departmentName) {
        if (departmentRepository.existsByName_En(departmentName.getEn())
                || departmentRepository.existsByName_Uk(departmentName.getUk())) {
            throw new DepartmentException("Department already exists");
        }

        Department department = Department.builder()
                .createdAt(LocalDateTime.now())
                .name(multiLanguageFieldMapper.toDomain(departmentName))
                .applications(new HashSet<>())
                .build();

        department = departmentRepository.save(department);
        return departmentMapper.toDto(department);
    }

    @Override
    @Transactional
    public DepartmentDto update(DepartmentUpdateRequest request) {
        Department department = departmentRepository.findById(request.id())
                .orElseThrow(() -> new DepartmentException("Department with ID: " + request.id() + " not found!"));

        department.setName(getOrDefault(request.name(), department.getName(), multiLanguageFieldMapper::toDomain));

        department.setUpdatedAt(LocalDateTime.now());

        department = departmentRepository.save(department);
        return departmentMapper.toDto(department);
    }

    @Override
    @Transactional
    public DepartmentDto getById(UUID departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentException("Department with ID: " + departmentId + " not found!"));
        return departmentMapper.toDto(department);
    }

    @Override
    @Transactional
    public Page<DepartmentDto> getAll(DepartmentGetAllRequest request) {
        int pageNumber = getOrDefault(request.pageNumber(), 0);
        int pageSize = getOrDefault(request.pageSize(), 10);
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        Page<Department> departments = departmentRepository.findAllBySearchQuery(paging, request);

        return departments.map(departmentMapper::toDto);
    }

    @Override
    @Transactional
    public void delete(UUID departmentId) {
    if (!departmentRepository.existsById(departmentId)) {
        throw new DepartmentException("Department with ID: " + departmentId + " not found!");
    }
        departmentRepository.deleteById(departmentId);
    }

    private <T, R> R getOrDefault(T newValue, R currentValue, Function<T, R> mapper) {
        return newValue != null ? Objects.requireNonNullElse(mapper.apply(newValue), currentValue) : currentValue;
    }

    private <T> T getOrDefault(T newValue, T currentValue) {
        return newValue != null ? newValue : currentValue;
    }
}
