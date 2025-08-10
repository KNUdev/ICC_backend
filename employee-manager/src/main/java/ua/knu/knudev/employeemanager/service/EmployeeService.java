package ua.knu.knudev.employeemanager.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import ua.knu.knudev.employeemanager.domain.Employee;
import ua.knu.knudev.employeemanager.domain.Sector;
import ua.knu.knudev.employeemanager.domain.Specialty;
import ua.knu.knudev.employeemanagerapi.exception.SectorException;
import ua.knu.knudev.employeemanagerapi.exception.SpecialtyException;
import ua.knu.knudev.icccommon.domain.embeddable.FullName;
import ua.knu.knudev.employeemanager.domain.embeddable.WorkHours;
import ua.knu.knudev.employeemanager.mapper.*;
import ua.knu.knudev.employeemanager.repository.EmployeeRepository;
import ua.knu.knudev.employeemanager.repository.SectorRepository;
import ua.knu.knudev.employeemanager.repository.SpecialtyRepository;
import ua.knu.knudev.employeemanagerapi.api.EmployeeApi;
import ua.knu.knudev.employeemanagerapi.dto.EmployeeDto;
import ua.knu.knudev.employeemanagerapi.dto.SectorDto;
import ua.knu.knudev.employeemanagerapi.dto.ShortEmployeeDto;
import ua.knu.knudev.employeemanagerapi.dto.SpecialtyDto;
import ua.knu.knudev.employeemanagerapi.exception.EmployeeException;
import ua.knu.knudev.employeemanagerapi.request.*;
import ua.knu.knudev.employeemanagerapi.response.AccountReceivingResponse;
import ua.knu.knudev.employeemanagerapi.response.GetEmployeeResponse;
import ua.knu.knudev.fileserviceapi.api.ImageServiceApi;
import ua.knu.knudev.fileserviceapi.subfolder.ImageSubfolder;
import ua.knu.knudev.icccommon.constant.EmployeeAdministrativeRole;
import ua.knu.knudev.icccommon.dto.FullNameDto;
import ua.knu.knudev.icccommon.dto.WorkHoursDto;
import ua.knu.knudev.icccommon.mapper.FullNameMapper;
import ua.knu.knudev.iccsecurityapi.api.EmployeeAuthServiceApi;
import ua.knu.knudev.iccsecurityapi.dto.AuthenticatedEmployeeDto;
import ua.knu.knudev.iccsecurityapi.request.AuthenticatedEmployeeUpdateRequest;
import ua.knu.knudev.iccsecurityapi.request.EmployeeRegistrationRequest;
import ua.knu.knudev.iccsecurityapi.response.EmployeeRegistrationResponse;

import javax.security.auth.login.AccountException;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private final ImageServiceApi imageServiceApi;
    private final EmployeeAuthServiceApi employeeAuthServiceApi;
    private final SectorRepository sectorRepository;
    private final SpecialtyRepository specialtyRepository;

    @Override
    public EmployeeDto create(@Valid EmployeeCreationRequest request) {
        FullName fullName = fullNameMapper.toDomain(request.fullName());
        WorkHours workHours = workHoursMapper.toDomain(request.workHours());

        Specialty specialty = specialtyRepository.findById(request.specialty().id()).orElseThrow(
                () -> new SpecialtyException("Specialty with id: " + request.specialty().id() + " not found"));
        Sector sector = sectorRepository.findById(request.sector().id()).orElseThrow(
                () -> new SectorException("Sector with id: " + request.sector().id() + " not found"));

        validateSectorNonExistence(request.sector());
        validateSpecialtyNonExistence(request.specialty());

        String uploadEmployeeImage = uploadEmployeeImage(
                request.avatarFile(),
                request.avatarFile().getOriginalFilename(),
                ImageSubfolder.EMPLOYEE_AVATARS
        );

        Employee employee = Employee.builder()
                .name(fullName)
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .salaryInUAH(request.salaryInUAH())
                .isStudent(request.isStudent())
                .avatar(uploadEmployeeImage)
                .contractEndDate(request.contractEndDate())
                .workHours(workHours)
                .role(request.role())
                .specialty(specialty)
                .sector(sector)
                .createdAt(LocalDateTime.now())
                .build();

        Employee savedEmployee = employeeRepository.save(employee);
        String avatarPath = imageServiceApi.getPathByFilename(savedEmployee.getAvatar(), ImageSubfolder.EMPLOYEE_AVATARS);

        log.info("Created Employee: {}", savedEmployee);
        return mapEmployeeToDto(savedEmployee, avatarPath);
    }

    @Override
    @Transactional
    public AccountReceivingResponse register(@Valid AccountReceivingRequest request) throws AccountException {
        if (!employeeRepository.existsByEmail(request.email())) {
            throw new AccountException("Employee with email:" + request.email() + " not found!");
        }

        Employee employee = employeeRepository.findByEmail(request.email())
                .orElseThrow(() -> new EmployeeException("Employee with email " + request.email() + " not found!"));

        EmployeeRegistrationRequest registrationRequest = EmployeeRegistrationRequest.builder()
                .email(request.email())
                .password(request.password())
                .role(employee.getRole())
                .build();

        EmployeeRegistrationResponse registrationResponse = employeeAuthServiceApi.create(registrationRequest);

        return AccountReceivingResponse.builder()
                .shortEmployeeDto(mapEmployeeToShortDto(
                        employee,
                        registrationResponse.email()))
                .responseMessage("The account was successfully registered!")
                .build();
    }

    @Override
    @Transactional
    public AccountReceivingResponse updateCredentials(@Valid AccountCredentialsUpdateRequest request) {
        Employee employee = employeeRepository.findById(request.id())
                .orElseThrow(() -> new EmployeeException("Employee with id " + request.id() + " not found!"));

        AuthenticatedEmployeeDto authenticatedEmployee = employeeAuthServiceApi.getByEmail(employee.getEmail());

        AuthenticatedEmployeeUpdateRequest updateRequest = buildAuthenticatedEmployeeUpdateRequest(
                authenticatedEmployee.id(),
                request.email(),
                request.oldPassword(),
                request.newPassword(),
                employee.getRole(),
                false
        );

        employeeAuthServiceApi.update(updateRequest);

        if (!employee.getEmail().equals(request.email()) &&
                request.email().matches("^[\\w.-]+@knu\\.ua$")) {
            employee.setEmail(request.email());
            employee = employeeRepository.save(employee);
        }

        return AccountReceivingResponse.builder()
                .shortEmployeeDto(mapEmployeeToShortDto(
                        employee,
                        employee.getEmail()
                ))
                .responseMessage("The account credentials was successfully updated!")
                .build();
    }

    @Override
    @Transactional
    public Page<GetEmployeeResponse> getAll(EmployeeReceivingRequest request) {
        int pageNumber = getOrDefault(request.pageNumber(), 0);
        int pageSize = getOrDefault(request.pageSize(), 10);
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        Page<Employee> employeePage = employeeRepository.findAllBySearchQuery(paging, request);

        return employeePage.map(this::mapEmployeeToResponse);
    }

    @Override
    @Transactional
    public EmployeeDto update(@Valid EmployeeUpdateRequest request) {
        Employee employee = getEmployeeById(request.id());

        if (request.workHours().getStartTime().after(request.workHours().getEndTime())
                || request.contractEndDate().isBefore(ChronoLocalDate.from(LocalDateTime.now()))) {
            throw new EmployeeException("Start time cannot be after end time in work hours");
        }

        AuthenticatedEmployeeDto authenticatedEmployee = employeeAuthServiceApi.getByEmail(employee.getEmail());

        employee.setUpdatedAt(LocalDateTime.now());
        employee.setName(getOrDefault(request.fullName(), employee.getName(), fullNameMapper::toDomain));
        employee.setSalaryInUAH(getOrDefault(request.salaryInUAH(), employee.getSalaryInUAH()));
        employee.setIsStudent(getOrDefault(request.isStudent(), employee.getIsStudent()));
        employee.setContractEndDate(getOrDefault(request.contractEndDate(), employee.getContractEndDate()));
        employee.setWorkHours(getOrDefault(request.workHours(), employee.getWorkHours(), workHoursMapper::toDomain));
        employee.setRole(getOrDefault(request.role(), employee.getRole()));

        if (request.phoneNumber().matches("^\\d{10,15}$")) {
            employee.setPhoneNumber(getOrDefault(request.phoneNumber(), employee.getPhoneNumber()));
        }
        if (specialtyRepository.existsById(request.specialty().id())) {
            employee.setSpecialty(getOrDefault(request.specialty(), employee.getSpecialty(), specialtyMapper::toDomain));
        }
        if (sectorRepository.existsById(request.sector().id())) {
            employee.setSector(getOrDefault(request.sector(), employee.getSector(), sectorMapper::toDomain));
        }
        if (request.avatarFile() != null) {
            updateAvatar(request.id(), request.avatarFile());
        }
        if (!employee.getEmail().equals(request.email()) && request.email().matches("^[\\w.-]+@knu\\.ua$")) {
            employee.setEmail(request.email());
        }

        AuthenticatedEmployeeUpdateRequest updateRequest = buildAuthenticatedEmployeeUpdateRequest(
                authenticatedEmployee.id(),
                request.email(),
                authenticatedEmployee.password(),
                request.password(),
                request.role(),
                true
        );

        employeeAuthServiceApi.update(updateRequest);
        Employee savedEmployee = employeeRepository.save(employee);

        return employeeMapper.toDto(savedEmployee);
    }

    @Override
    public GetEmployeeResponse getById(UUID id) {
        Employee employee = getEmployeeById(id);
        return mapEmployeeToResponse(employee);
    }

    @Override
    public String updateAvatar(UUID id, MultipartFile avatarFile) {
        Employee employee = getEmployeeById(id);

        String oldAvatarFilename = employee.getAvatar();
        String newAvatarFilename = imageServiceApi.updateByFilename(oldAvatarFilename, avatarFile, ImageSubfolder.EMPLOYEE_AVATARS);

        employee.setAvatar(newAvatarFilename);
        employee.setUpdatedAt(LocalDateTime.now());
        employeeRepository.save(employee);

        return imageServiceApi.getPathByFilename(newAvatarFilename, ImageSubfolder.EMPLOYEE_AVATARS);
    }

    @Override
    public String addAvatar(MultipartFile avatarFile) {
        return imageServiceApi.uploadFile(avatarFile, ImageSubfolder.EMPLOYEE_AVATARS);
    }

    @Override
    public void removeAvatar(UUID employeeId) {
        Employee employee = getEmployeeById(employeeId);
        String avatarFilename = employee.getAvatar();
        if (StringUtils.isEmpty(avatarFilename)) {
            throw new EmployeeException("Employee with id: " + employeeId + " does not have an avatar");
        }

        imageServiceApi.removeByFilename(avatarFilename, ImageSubfolder.EMPLOYEE_AVATARS);
        employee.setAvatar(null);
        employeeRepository.save(employee);
    }

    @Override
    public Set<GetEmployeeResponse> getAll() {
        return employeeRepository.findAll().stream()
                .map(this::mapEmployeeToResponse)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        employeeRepository.findById(id).ifPresent(employee ->
                employeeAuthServiceApi.deleteByEmail(employee.getEmail()));
        employeeRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return employeeRepository.existsById(id);
    }

    private GetEmployeeResponse mapEmployeeToResponse(Employee employee) {
        FullNameDto fullNameDto = fullNameMapper.toDto(employee.getName());
        WorkHoursDto workHoursDto = workHoursMapper.toDto(employee.getWorkHours());
        SpecialtyDto specialtyDto = specialtyMapper.toDto(employee.getSpecialty());
        SectorDto sectorDto = sectorMapper.toDto(employee.getSector());
        String avatarUrl = employee.getAvatar() == null
                ? null
                : imageServiceApi.getPathByFilename(employee.getAvatar(), ImageSubfolder.EMPLOYEE_AVATARS);

        return new GetEmployeeResponse(
                employee.getId(),
                fullNameDto,
                employee.getEmail(),
                employee.getPhoneNumber(),
                employee.getCreatedAt(),
                employee.getUpdatedAt(),
                employee.getSalaryInUAH(),
                employee.getIsStudent(),
                avatarUrl,
                employee.getContractEndDate(),
                workHoursDto,
                employee.getRole(),
                specialtyDto,
                sectorDto
        );
    }

    private EmployeeDto mapEmployeeToDto(Employee employee, String avatarPath) {
        WorkHoursDto workHours = workHoursMapper.toDto(employee.getWorkHours());
        SectorDto sector = sectorMapper.toDto(employee.getSector());
        SpecialtyDto specialty = specialtyMapper.toDto(employee.getSpecialty());

        return EmployeeDto.builder()
                .id(employee.getId())
                .name(FullNameDto.builder()
                        .firstName(employee.getName().getFirstName())
                        .lastName(employee.getName().getLastName())
                        .middleName(employee.getName().getMiddleName())
                        .build())
                .email(employee.getEmail())
                .phoneNumber(employee.getPhoneNumber())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .salaryInUAH(employee.getSalaryInUAH())
                .isStudent(employee.getIsStudent())
                .avatar(avatarPath)
                .contractEndDate(employee.getContractEndDate())
                .role(employee.getRole())
                .workHours(workHours)
                .specialty(specialty)
                .sector(sector)
                .build();
    }

    private ShortEmployeeDto mapEmployeeToShortDto(
            Employee employee,
            String updatedEmail
    ) {
        return ShortEmployeeDto.builder()
                .fullNameDto(
                        FullNameDto.builder()
                                .firstName(employee.getName().getFirstName())
                                .middleName(employee.getName().getMiddleName())
                                .lastName(employee.getName().getLastName())
                                .build()
                )
                .updatedAt(LocalDateTime.now())
                .email(updatedEmail)
                .build();
    }

    private AuthenticatedEmployeeUpdateRequest buildAuthenticatedEmployeeUpdateRequest(
            UUID authenticatedEmployeeId,
            String email,
            String oldPassword,
            String newPassword,
            EmployeeAdministrativeRole role,
            boolean isAdminUsage
    ) {
        return AuthenticatedEmployeeUpdateRequest.builder()
                .employeeId(authenticatedEmployeeId)
                .email(email)
                .oldPassword(oldPassword)
                .newPassword(newPassword)
                .role(role)
                .isAdminUsage(isAdminUsage)
                .build();
    }

    private Employee getEmployeeById(UUID id) {
        return employeeRepository.findById(id).orElseThrow(
                () -> new EmployeeException("Employee with id " + id + " not found"));
    }

    private String uploadEmployeeImage(MultipartFile problemPhoto, String imageName, ImageSubfolder subfolder) {
        if (ObjectUtils.isEmpty(problemPhoto)) {
            return null;
        }
        return imageServiceApi.uploadFile(problemPhoto, imageName, subfolder);
    }

    private <T> T getOrDefault(T newValue, T currentValue) {
        return newValue != null ? newValue : currentValue;
    }

    private <T, R> R getOrDefault(T newValue, R currentValue, Function<T, R> mapper) {
        return newValue != null ? Objects.requireNonNullElse(mapper.apply(newValue), currentValue) : currentValue;
    }

    private void validateSectorNonExistence(SectorDto sector) {
        sectorRepository.findById(sector.id()).orElseThrow(
                () -> new EmployeeException("Sector (passed to employee) with id " + sector.id() + " does not exist")
        );
    }

    private void validateSpecialtyNonExistence(SpecialtyDto specialty) {
        specialtyRepository.findById(specialty.id()).orElseThrow(
                () -> new EmployeeException("Specialty (passed to employee) with id " + specialty.id() + " does not exist")
        );
    }
}
