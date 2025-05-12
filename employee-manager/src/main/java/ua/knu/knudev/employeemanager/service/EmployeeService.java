package ua.knu.knudev.employeemanager.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ua.knu.knudev.employeemanager.domain.Employee;
import ua.knu.knudev.employeemanager.domain.Sector;
import ua.knu.knudev.employeemanager.domain.Specialty;
import ua.knu.knudev.employeemanager.domain.embeddable.FullName;
import ua.knu.knudev.employeemanager.domain.embeddable.WorkHours;
import ua.knu.knudev.employeemanager.mapper.*;
import ua.knu.knudev.employeemanager.repository.EmployeeRepository;
import ua.knu.knudev.employeemanagerapi.api.EmployeeApi;
import ua.knu.knudev.employeemanagerapi.dto.EmployeeDto;
import ua.knu.knudev.employeemanagerapi.exception.EmployeeException;
import ua.knu.knudev.employeemanagerapi.request.EmployeeCreationRequest;
import ua.knu.knudev.employeemanagerapi.request.EmployeeReceivingRequest;
import ua.knu.knudev.employeemanagerapi.request.EmployeeUpdateRequest;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class EmployeeService implements EmployeeApi {

    private final FullNameMapper fullNameMapper;
    private final WorkHoursMapper workHoursMapper;
    private final SpecialtyMapper specialtyMapper;
    private final SectorMapper sectorMapper;
    private final EmployeeMapper employeeMapper;
    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeDto create(EmployeeCreationRequest request) {
        FullName fullName = fullNameMapper.toDomain(request.fullName());
        WorkHours workHours = workHoursMapper.toDomain(request.workHours());
        Specialty specialty = specialtyMapper.toDomain(request.specialty());
        Sector sector = sectorMapper.toDomain(request.sector());

        Employee employee = Employee.builder()
                .name(fullName)
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .salaryInUAH(request.salaryInUAH())
                .isStudent(request.isStudent())
                .avatar(request.avatar())
                .contractEndDate(request.contractEndDate())
                .workHours(workHours)
                .role(request.role())
                .specialty(specialty)
                .sector(sector)
                .createdAt(LocalDateTime.now())
                .build();

        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Created Employee: {}", savedEmployee);
        return employeeMapper.toDto(savedEmployee);
    }

    public Page<EmployeeDto> getAll(EmployeeReceivingRequest request) {
        Pageable paging = PageRequest.of(request.pageNumber(), request.pageSize());
        Page<Employee> employeePage = employeeRepository.findAllBySearchQuery(paging, request);

        return employeePage.map(employeeMapper::toDto);
    }

    @Override
    @Transactional
    public EmployeeDto update(EmployeeUpdateRequest request) {
        Employee employee = getEmployeeById(request.id());

        checkIfEmailIsValid(request.email());

        employee.setUpdatedAt(LocalDateTime.now());
        employee.setName(getOrDefault(request.fullName(), employee.getName(),
                fullNameMapper::toDomain
        ));
        employee.setEmail(getOrDefault(request.email(), employee.getEmail()));
        employee.setSalaryInUAH(getOrDefault(request.salaryInUAH(), employee.getSalaryInUAH()));
        employee.setIsStudent(getOrDefault(request.isStudent(), employee.getIsStudent()));
        employee.setAvatar(getOrDefault(request.avatar(), employee.getAvatar()));
        employee.setContractEndDate(getOrDefault(request.contractEndDate(), employee.getContractEndDate()));
        employee.setWorkHours(getOrDefault(request.workHours(), employee.getWorkHours(),
                workHoursMapper::toDomain
        ));
        employee.setRole(getOrDefault(request.role(), employee.getRole()));
        employee.setSpecialty(getOrDefault(request.specialty(), employee.getSpecialty(),
                specialtyMapper::toDomain
        ));
        employee.setSector(getOrDefault(request.sector(), employee.getSector(),
                sectorMapper::toDomain
        ));

        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDto(savedEmployee);
    }

    @Override
    public EmployeeDto getById(UUID id) {
        Employee employee = getEmployeeById(id);
        return employeeMapper.toDto(employee);
    }

    @Override
    public void deleteById(UUID id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return employeeRepository.existsById(id);
    }


    private Employee getEmployeeById(UUID id) {
        return employeeRepository.findById(id).orElseThrow(
                () -> new EmployeeException("Employee with id " + id + " not found"));
    }

    private <T> T getOrDefault(T newValue, T currentValue) {
        return newValue != null ? newValue : currentValue;
    }

    private <T, R> R getOrDefault(T newValue, R currentValue, Function<T, R> mapper) {
        return newValue != null ? Objects.requireNonNullElse(mapper.apply(newValue), currentValue) : currentValue;
    }

    private void checkIfEmailIsValid(String email) {
        if (email != null && !email.matches("^[\\w.-]+@knu\\.ua$")) {
            throw new EmployeeException("Invalid email address:" + email);
        }
    }
}
