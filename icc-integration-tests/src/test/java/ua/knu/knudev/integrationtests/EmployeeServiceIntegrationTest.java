package ua.knu.knudev.integrationtests;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import ua.knu.knudev.employeemanager.domain.Employee;
import ua.knu.knudev.employeemanager.domain.Sector;
import ua.knu.knudev.employeemanager.domain.Specialty;
import ua.knu.knudev.employeemanager.domain.embeddable.FullName;
import ua.knu.knudev.employeemanager.domain.embeddable.MultiLanguageField;
import ua.knu.knudev.employeemanager.domain.embeddable.WorkHours;
import ua.knu.knudev.employeemanager.mapper.*;
import ua.knu.knudev.employeemanager.repository.EmployeeRepository;
import ua.knu.knudev.employeemanager.repository.SectorRepository;
import ua.knu.knudev.employeemanager.repository.SpecialtyRepository;
import ua.knu.knudev.employeemanager.service.EmployeeService;
import ua.knu.knudev.employeemanagerapi.dto.EmployeeDto;
import ua.knu.knudev.employeemanagerapi.exception.EmployeeException;
import ua.knu.knudev.employeemanagerapi.request.EmployeeCreationRequest;
import ua.knu.knudev.employeemanagerapi.request.EmployeeReceivingRequest;
import ua.knu.knudev.employeemanagerapi.request.EmployeeUpdateRequest;
import ua.knu.knudev.employeemanagerapi.response.GetEmployeeResponse;
import ua.knu.knudev.fileserviceapi.api.ImageServiceApi;
import ua.knu.knudev.fileserviceapi.subfolder.ImageSubfolder;
import ua.knu.knudev.icccommon.constant.SpecialtyCategory;
import ua.knu.knudev.icccommon.dto.FullNameDto;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;
import ua.knu.knudev.icccommon.dto.WorkHoursDto;
import ua.knu.knudev.integrationtests.config.IntegrationTestsConfig;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ua.knu.knudev.integrationtests.utils.constants.EmployeeTestsConstants.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = IntegrationTestsConfig.class)
@ActiveProfiles("test")
public class EmployeeServiceIntegrationTest {

    @Autowired
    private SpecialtyMapper specialtyMapper;
    @Autowired
    private SectorMapper sectorMapper;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private SectorRepository sectorRepository;
    @Autowired
    private SpecialtyRepository specialtyRepository;
    @Autowired
    private ImageServiceApi imageServiceApi;
    @Autowired
    private EmployeeService employeeService;

    private Sector testSector;
    private Specialty testSpecialty;
    private Employee testEmployee;

    private final List<String> uploadedAvatarFiles = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        testSector = createTestSector();
        testSpecialty = createTestSpecialty();
        testEmployee = createTestEmployee();
    }

    @AfterEach
    public void tearDown() {
        uploadedAvatarFiles.forEach(uploadedAvatarFile -> {
            imageServiceApi.removeByFilename(uploadedAvatarFile, ImageSubfolder.EMPLOYEE_AVATARS);
        });
        uploadedAvatarFiles.clear();
        employeeRepository.deleteAll();
        sectorRepository.deleteAll();
        specialtyRepository.deleteAll();
    }

    private Sector createTestSector() {
        Sector sector = new Sector();
        MultiLanguageField name = new MultiLanguageField(TEST_SECTOR_NAME_IN_ENGLISH, TEST_SECTOR_NAME_IN_UKRAINIAN);

        sector.setCreatedAt(LocalDateTime.of(2015, 1, 1, 0, 0));
        sector.setUpdatedAt(LocalDateTime.of(2022, 1, 1, 0, 0));
        sector.setName(name);
        Sector newSector = sectorRepository.save(sector);
        return newSector;
    }

    private Specialty createTestSpecialty() {
        Specialty specialty = new Specialty();
        specialty.setName(new MultiLanguageField(TEST_SPECIALTY_NAME_IN_ENGLISH, TEST_SPECIALTY_NAME_IN_UKRAINIAN));
        specialty.setCreatedAt(LocalDateTime.of(2015, 1, 1, 0, 0));
        specialty.setUpdatedAt(LocalDateTime.of(2022, 1, 1, 0, 0));
        specialty.setCategory(SpecialtyCategory.SENIOR);
        specialty.addSector(testSector);

        Specialty newSpecialty = specialtyRepository.save(specialty);
        return newSpecialty;
    }

    private Employee createTestEmployee() {
        Employee employee = new Employee();
        FullName fullName = new FullName(TEST_EMPLOYEE_FIRST_NAME, TEST_EMPLOYEE_MIDDLE_NAME, TEST_EMPLOYEE_LAST_NAME);
        WorkHours workHours = new WorkHours(TEST_EMPLOYEE_WORK_START_TIME, TEST_EMPLOYEE_WORK_END_TIME);

        String avatarFileName = imageServiceApi.uploadFile(TEST_AVATAR_FILE, ImageSubfolder.EMPLOYEE_AVATARS);

        employee.setName(fullName);
        employee.setEmail(TEST_EMPLOYEE_EMAIL);
        employee.setPhoneNumber(TEST_EMPLOYEE_PHONE_NUMBER);
        employee.setCreatedAt(TEST_EMPLOYEE_CREATED_AT);
        employee.setUpdatedAt(TEST_EMPLOYEE_UPDATED_AT);
        employee.setSalaryInUAH(TEST_EMPLOYEE_SALARY_IN_UAH);
        employee.setIsStudent(TEST_EMPLOYEE_IS_STUDENT);
        employee.setAvatar(avatarFileName);
        uploadedAvatarFiles.add(avatarFileName);
        employee.setContractEndDate(TEST_EMPLOYEE_CONTRACT_END_DATE);
        employee.setWorkHours(workHours);
        employee.setRole(TEST_EMPLOYEE_ROLE);
        employee.setSector(testSector);
        employee.setSpecialty(testSpecialty);

        Employee newEmployee = employeeRepository.save(employee);
        return newEmployee;
    }

    EmployeeCreationRequest getEmployeeCreationRequest(String phoneNumber, Sector sector, Specialty specialty) {
        FullNameDto fullNameDto = new FullNameDto(TEST_EMPLOYEE_FIRST_NAME, TEST_EMPLOYEE_MIDDLE_NAME, TEST_EMPLOYEE_LAST_NAME);
        WorkHoursDto workHoursDto = new WorkHoursDto(TEST_EMPLOYEE_WORK_START_TIME, TEST_EMPLOYEE_WORK_END_TIME);

        EmployeeCreationRequest request = EmployeeCreationRequest.builder()
                .fullName(fullNameDto)
                .email(TEST_EMPLOYEE_EMAIL)
                .phoneNumber(phoneNumber)
                .salaryInUAH(TEST_EMPLOYEE_SALARY_IN_UAH)
                .isStudent(TEST_EMPLOYEE_IS_STUDENT)
                .avatarFile(TEST_AVATAR_FILE)
                .contractEndDate(TEST_EMPLOYEE_CONTRACT_END_DATE)
                .workHours(workHoursDto)
                .role(TEST_EMPLOYEE_ROLE)
                .specialty(specialtyMapper.toDto(specialty))
                .sector(sectorMapper.toDto(sector))
                .build();

        return request;
    }

    EmployeeUpdateRequest getEmployeeUpdateRequest(String email, String phoneNumber, Sector sector) {
        EmployeeUpdateRequest request = new EmployeeUpdateRequest(
                testEmployee.getId(),
                null,
                email,
                phoneNumber,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                sectorMapper.toDto(sector)
        );

        return request;
    }

    void createManyEmployees() {
        EmployeeCreationRequest creationRequest = getEmployeeCreationRequest(TEST_EMPLOYEE_PHONE_NUMBER, testSector, testSpecialty);
        for (int i = 0; i < 9; i++) {
            createTestEmployee();
        }
    }

    EmployeeReceivingRequest getEmployeeReceivingRequest(String searchQuery) {
        LocalDateTime createdBefore = LocalDateTime.now().plusWeeks(1);
        LocalDateTime createdAfter = TEST_EMPLOYEE_CREATED_AT.minusWeeks(1);
        LocalDateTime updatedBefore = LocalDateTime.now().plusWeeks(1);
        LocalDateTime updatedAfter = TEST_EMPLOYEE_UPDATED_AT.minusWeeks(1);
        LocalDate contractEndBefore = TEST_EMPLOYEE_CONTRACT_END_DATE.plusWeeks(1);
        LocalDate contractEndAfter = TEST_EMPLOYEE_CONTRACT_END_DATE.minusWeeks(1);

        EmployeeReceivingRequest request = new EmployeeReceivingRequest(
                searchQuery,
                TEST_EMPLOYEE_EMAIL,
                TEST_EMPLOYEE_PHONE_NUMBER,
                createdBefore,
                createdAfter,
                updatedBefore,
                updatedAfter,
                TEST_EMPLOYEE_SALARY_IN_UAH,
                TEST_EMPLOYEE_IS_STUDENT,
                null,
                contractEndBefore,
                contractEndAfter,
                new WorkHoursDto(TEST_EMPLOYEE_WORK_START_TIME, TEST_EMPLOYEE_WORK_END_TIME),
                TEST_EMPLOYEE_ROLE,
                new MultiLanguageFieldDto(testSpecialty.getName().getEn(), testSpecialty.getName().getUk()),
                new MultiLanguageFieldDto(testSector.getName().getEn(), testSector.getName().getUk()),
                0,
                10
        );

        return request;
    }

    @Nested
    @DisplayName("Get by id scenarios")
    class GetByIdScenarios {
        @Test
        @DisplayName("Should successfully get Employee when provided valid id")
        @Transactional
        void should_SuccessfullyGetEmployee_When_ProvidedValidId() {
            GetEmployeeResponse response = employeeService.getById(testEmployee.getId());

            assertNotNull(response);
            assertEquals(testEmployee.getName().getFirstName(), response.name().getFirstName());
            assertEquals(testEmployee.getEmail(), response.email());
            assertEquals(testEmployee.getPhoneNumber(), response.phoneNumber());
            assertEquals(testEmployee.getRole(), response.role());
            assertEquals(testEmployee.getSector().getName().getEn(), response.sector().name().getEn());
            assertEquals(testEmployee.getSpecialty().getName().getEn(), response.specialty().name().getEn());
        }

        @Test
        @DisplayName("Should throw EmployeeException when provided invalid id")
        void should_ThrowEmployeeException_When_ProvidedInvalidId() {
            assertThrows(EmployeeException.class, () -> employeeService.getById(UUID.randomUUID()));
        }
    }

    @Nested
    @DisplayName("Create Employee scenarios")
    class CreateEmployeeScenarios {
        @Test
        @DisplayName("should successfully create employee when provided valid request")
        void should_SuccessfullyCreateEmployee_When_ProvidedValidRequest() {
            EmployeeCreationRequest request = getEmployeeCreationRequest(TEST_EMPLOYEE_PHONE_NUMBER, testSector, testSpecialty);
            EmployeeDto response = employeeService.create(request);

            assertNotNull(response);
            uploadedAvatarFiles.add(response.avatar());
            assertEquals(TEST_EMPLOYEE_FIRST_NAME, response.name().getFirstName());
            assertEquals(TEST_EMPLOYEE_EMAIL, response.email());
            assertEquals(TEST_EMPLOYEE_PHONE_NUMBER, response.phoneNumber());
            assertEquals(TEST_EMPLOYEE_ROLE, response.role());
            assertTrue(employeeRepository.existsById(response.id()));
        }

        @Test
        @DisplayName("Should throw ConstraintViolationException when provided invalid phone number")
        void should_ThrowConstraintViolationException_When_ProvidedInvalidPhoneNumber() {
            EmployeeCreationRequest request = getEmployeeCreationRequest("1234", testSector, testSpecialty);

            assertThrows(ConstraintViolationException.class, () -> employeeService.create(request));
        }

        @Test
        @DisplayName("Should throw EmployeeException when provided non-existent sector")
        void should_ThrowEmployeeException_When_ProvidedNonExistentSector() {
            Sector nonExistingSector = new Sector();
            nonExistingSector.setId(UUID.randomUUID());
            nonExistingSector.setName(new MultiLanguageField(TEST_SECTOR_NAME_IN_ENGLISH, TEST_SECTOR_NAME_IN_UKRAINIAN));

            EmployeeCreationRequest request = getEmployeeCreationRequest(TEST_EMPLOYEE_PHONE_NUMBER, nonExistingSector, testSpecialty);
            assertThrows(EmployeeException.class, () -> employeeService.create(request));
        }

        @Test
        @DisplayName("Should throw EmployeeException when provided non-existent specialty")
        void should_ThrowEmployeeException_When_ProvidedNonExistentSpecialty() {
            Specialty nonExistingSpecialty = new Specialty();
            nonExistingSpecialty.setId(UUID.randomUUID());
            nonExistingSpecialty.setName(new MultiLanguageField(TEST_SPECIALTY_NAME_IN_ENGLISH, TEST_SPECIALTY_NAME_IN_UKRAINIAN));

            EmployeeCreationRequest request = getEmployeeCreationRequest(TEST_EMPLOYEE_PHONE_NUMBER, testSector, nonExistingSpecialty);
            assertThrows(EmployeeException.class, () -> employeeService.create(request));
        }
    }

    @Nested
    @DisplayName("Update employee scenarios")
    class UpdateEmployeeScenarios {
        @Test
        @DisplayName("Should successfully update employee when provided valid data")
        void should_SuccessfullyUpdateEmployee_When_ProvidedValidData() {
            String newPhoneNumber = "1234567890";

            EmployeeUpdateRequest request = getEmployeeUpdateRequest(null, newPhoneNumber, null);

            EmployeeDto response = employeeService.update(request);

            assertNotNull(response);
            uploadedAvatarFiles.removeLast();
            uploadedAvatarFiles.add(response.avatar());
            assertEquals(TEST_EMPLOYEE_FIRST_NAME, response.name().getFirstName());
            assertEquals(TEST_EMPLOYEE_EMAIL, response.email());
            assertEquals(newPhoneNumber, response.phoneNumber());
            assertEquals(TEST_EMPLOYEE_ROLE, response.role());
            assertEquals(testSector.getId(), response.sector().id());
            assertEquals(testSpecialty.getId(), response.specialty().id());
            assertTrue(employeeRepository.existsById(response.id()));
        }

        @Test
        @DisplayName("Should throw EmployeeException when provided invalid email")
        void should_ThrowEmployeeException_When_ProvidedInvalidEmail() {
            String invalidEmail = "invalid@email.com";

            EmployeeUpdateRequest request = getEmployeeUpdateRequest(invalidEmail, null, null);

            assertThrows(EmployeeException.class, () -> employeeService.update(request));
        }

        @Test
        @DisplayName("Should throw EmployeeException when provided non-existent sector")
        void should_ThrowEmployeeException_When_ProvidedNonExistentSector() {
            Sector nonExistingSector = new Sector();
            nonExistingSector.setId(UUID.randomUUID());
            nonExistingSector.setName(new MultiLanguageField(TEST_SPECIALTY_NAME_IN_ENGLISH, TEST_SPECIALTY_NAME_IN_UKRAINIAN));

            EmployeeUpdateRequest request = getEmployeeUpdateRequest(null, null, nonExistingSector);

            assertThrows(EmployeeException.class, () -> employeeService.update(request));
        }
    }

    @Nested
    @DisplayName("Get all scenarios")
    class GetAllScenarios {
        @Test
        @DisplayName("Should successfully get all employees when provided appropriate request")
        void should_SuccessfullyGetAll_When_ProvidedAppropriateRequest() {
            createManyEmployees();

            EmployeeReceivingRequest request = getEmployeeReceivingRequest(TEST_EMPLOYEE_FIRST_NAME);

            Page<GetEmployeeResponse> response = employeeService.getAll(request);

            assertNotNull(request);
            assertEquals(10, employeeRepository.count());
            assertEquals(10, response.getTotalElements());
        }

        @Test
        @DisplayName("Should get zero employees when provided not existing search query")
        void should_getZeroEmployees_When_ProvidedNotExistingSearchQuery() {
            createManyEmployees();

            EmployeeReceivingRequest request = getEmployeeReceivingRequest("not-existing-search-query");

            Page<GetEmployeeResponse> response = employeeService.getAll(request);

            assertNotNull(request);
            assertEquals(10, employeeRepository.count());
            assertEquals(0, response.getTotalElements());
        }
    }
}
