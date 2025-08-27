package ua.knu.knudev.integrationtests;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;
import ua.knu.knudev.employeemanager.domain.Employee;
import ua.knu.knudev.employeemanager.domain.Sector;
import ua.knu.knudev.employeemanager.domain.Specialty;
import ua.knu.knudev.employeemanager.domain.embeddable.WorkHours;
import ua.knu.knudev.employeemanager.mapper.SectorMapper;
import ua.knu.knudev.employeemanager.mapper.SpecialtyMapper;
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
import ua.knu.knudev.icccommon.constant.EmployeeAdministrativeRole;
import ua.knu.knudev.icccommon.constant.SpecialtyCategory;
import ua.knu.knudev.icccommon.domain.embeddable.FullName;
import ua.knu.knudev.icccommon.domain.embeddable.MultiLanguageField;
import ua.knu.knudev.icccommon.dto.FullNameDto;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;
import ua.knu.knudev.icccommon.dto.WorkHoursDto;
import ua.knu.knudev.integrationtests.config.IntegrationTestsConfig;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = IntegrationTestsConfig.class)
@ActiveProfiles("test")
public class EmployeeServiceIntegrationTest {

    private final List<String> uploadedAvatarFiles = new ArrayList<>();

    public static final String TEST_EMPLOYEE_FIRST_NAME = "EmployeeFirstName";
    public static final String TEST_EMPLOYEE_MIDDLE_NAME = "EmployeeMiddleName";
    public static final String TEST_EMPLOYEE_LAST_NAME = "EmployeeLastName";
    public static final String TEST_EMPLOYEE_EMAIL = "ivan@knu.ua";
    public static final String TEST_EMPLOYEE_PHONE_NUMBER = "380960000000";
    public static final LocalDateTime TEST_EMPLOYEE_CREATED_AT = LocalDateTime.of(2019, 1, 1, 0, 0, 0);
    public static final LocalDateTime TEST_EMPLOYEE_UPDATED_AT = LocalDateTime.of(2020, 1, 1, 0, 0);
    public static final Double TEST_EMPLOYEE_SALARY_IN_UAH = 18000.;
    public static final Boolean TEST_EMPLOYEE_IS_STUDENT = true;
    public static final MultipartFile TEST_AVATAR_FILE = new MockMultipartFile("avatar.jpg", "avatar.jpeg", "image/jpeg", "image".getBytes());
    public static final LocalDate TEST_EMPLOYEE_CONTRACT_END_DATE = LocalDate.of(2026, 1, 1);
    public static final Time TEST_EMPLOYEE_WORK_START_TIME = Time.valueOf("09:00:00");
    public static final Time TEST_EMPLOYEE_WORK_END_TIME = Time.valueOf("16:00:00");
    public static final EmployeeAdministrativeRole TEST_EMPLOYEE_ROLE = EmployeeAdministrativeRole.COMMON_USER;

    public static final String TEST_SECTOR_NAME_IN_ENGLISH = "test-sector-name";
    public static final String TEST_SECTOR_NAME_IN_UKRAINIAN = "тестове-ім'я-сектора";

    public static final String TEST_SPECIALTY_NAME_IN_ENGLISH = "test-specialty-name";
    public static final String TEST_SPECIALTY_NAME_IN_UKRAINIAN = "тестове-ім'я-спеціальності";

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


    @BeforeEach
    public void setUp() {
        testSector = createTestSector();
        testSpecialty = createTestSpecialty(testSector);
        testEmployee = createTestEmployee();
    }

    @AfterEach
    public void tearDown() {
        uploadedAvatarFiles.forEach(uploadedAvatarFile -> {
            try {
                imageServiceApi.removeByFilename(uploadedAvatarFile, ImageSubfolder.EMPLOYEE_AVATARS);
            } catch (Exception ignored) {
            }
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

    private Specialty createTestSpecialty(Sector sector) {
        Specialty specialty = new Specialty();
        specialty.setName(new MultiLanguageField(TEST_SPECIALTY_NAME_IN_ENGLISH, TEST_SPECIALTY_NAME_IN_UKRAINIAN));
        specialty.setCreatedAt(LocalDateTime.of(2015, 1, 1, 0, 0));
        specialty.setUpdatedAt(LocalDateTime.of(2022, 1, 1, 0, 0));
        specialty.setCategory(SpecialtyCategory.SENIOR);
        specialty.addSector(sector);

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

    private EmployeeCreationRequest getEmployeeCreationRequest(String phoneNumber, Sector sector, Specialty specialty, String email) {
        FullNameDto fullNameDto = new FullNameDto(TEST_EMPLOYEE_FIRST_NAME, TEST_EMPLOYEE_MIDDLE_NAME, TEST_EMPLOYEE_LAST_NAME);
        WorkHoursDto workHoursDto = new WorkHoursDto(TEST_EMPLOYEE_WORK_START_TIME, TEST_EMPLOYEE_WORK_END_TIME);

        EmployeeCreationRequest request = EmployeeCreationRequest.builder()
                .fullName(fullNameDto)
                .email(email)
                .phoneNumber(phoneNumber)
                .salaryInUAH(TEST_EMPLOYEE_SALARY_IN_UAH)
                .isStudent(TEST_EMPLOYEE_IS_STUDENT)
                .avatarFile(TEST_AVATAR_FILE)
                .contractEndDate(TEST_EMPLOYEE_CONTRACT_END_DATE)
                .workHours(workHoursDto)
                .role(TEST_EMPLOYEE_ROLE)
                .specialtyId(specialty.getId())
                .sectorId(sector.getId())
                .build();

        return request;
    }

    private EmployeeUpdateRequest getEmployeeUpdateRequest(String email, Sector sector) {
        EmployeeUpdateRequest request = new EmployeeUpdateRequest(
                testEmployee.getId(),
                null,
                email,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                sector.getId()
        );

        return request;
    }

    private void createManyEmployees() {
        for (int i = 0; i < 9; i++) {
            createTestEmployee();
        }
    }

    private EmployeeReceivingRequest getEmployeeReceivingRequest(String searchQuery) {
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
        public void should_SuccessfullyGetEmployee_When_ProvidedValidId() {
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
        public void should_ThrowEmployeeException_When_ProvidedInvalidId() {
            assertThrows(EmployeeException.class, () -> employeeService.getById(UUID.randomUUID()));
        }
    }

    @Nested
    @DisplayName("Create Employee scenarios")
    class CreateEmployeeScenarios {
        @Transactional
        @Test
        @DisplayName("should successfully create employee when provided valid data in request")
        public void should_SuccessfullyCreateEmployee_When_ProvidedValidDataInRequest() {
            String email = "123" + TEST_EMPLOYEE_EMAIL;
            EmployeeCreationRequest request = getEmployeeCreationRequest(TEST_EMPLOYEE_PHONE_NUMBER, testSector, testSpecialty, email);
            EmployeeDto response = employeeService.create(request);

            assertNotNull(response);
            uploadedAvatarFiles.add(response.avatar());
            assertEquals(TEST_EMPLOYEE_FIRST_NAME, response.name().getFirstName());
            assertEquals(email, response.email());
            assertEquals(TEST_EMPLOYEE_PHONE_NUMBER, response.phoneNumber());
            assertEquals(TEST_EMPLOYEE_ROLE, response.role());
            assertTrue(employeeRepository.existsById(response.id()));
        }

        @Test
        @DisplayName("Should throw ConstraintViolationException when provided invalid phone number")
        public void should_ThrowConstraintViolationException_When_ProvidedInvalidPhoneNumber() {
            EmployeeCreationRequest request = getEmployeeCreationRequest("1234", testSector, testSpecialty, TEST_EMPLOYEE_EMAIL);

            assertThrows(ConstraintViolationException.class, () -> employeeService.create(request));
        }

        @Test
        @DisplayName("Should throw EmployeeException when provided non-existent sector")
        public void should_ThrowEmployeeException_When_ProvidedNonExistentSector() {
            Sector nonExistingSector = new Sector();
            nonExistingSector.setId(UUID.randomUUID());
            nonExistingSector.setName(new MultiLanguageField(TEST_SECTOR_NAME_IN_ENGLISH, TEST_SECTOR_NAME_IN_UKRAINIAN));

            EmployeeCreationRequest request = getEmployeeCreationRequest(TEST_EMPLOYEE_PHONE_NUMBER, nonExistingSector, testSpecialty, TEST_EMPLOYEE_EMAIL);
            assertThrows(EmployeeException.class, () -> employeeService.create(request));
        }

        @Test
        @DisplayName("Should throw EmployeeException when provided non-existent specialty")
        public void should_ThrowEmployeeException_When_ProvidedNonExistentSpecialty() {
            Specialty nonExistingSpecialty = new Specialty();
            nonExistingSpecialty.setId(UUID.randomUUID());
            nonExistingSpecialty.setName(new MultiLanguageField(TEST_SPECIALTY_NAME_IN_ENGLISH, TEST_SPECIALTY_NAME_IN_UKRAINIAN));

            EmployeeCreationRequest request = getEmployeeCreationRequest(TEST_EMPLOYEE_PHONE_NUMBER, testSector, nonExistingSpecialty, TEST_EMPLOYEE_EMAIL);
            assertThrows(EmployeeException.class, () -> employeeService.create(request));
        }
    }

    @Nested
    @DisplayName("Get all scenarios")
    class GetAllScenarios {
        @Test
        @DisplayName("Should successfully get all employees when provided appropriate request")
        public void should_SuccessfullyGetAll_When_ProvidedAppropriateRequest() {
            createManyEmployees();

            EmployeeReceivingRequest request = getEmployeeReceivingRequest(TEST_EMPLOYEE_FIRST_NAME);

            Page<GetEmployeeResponse> response = employeeService.getAll(request);

            assertNotNull(request);
            assertEquals(11, employeeRepository.count());
            assertEquals(10, response.getTotalElements());
        }

        @Test
        @DisplayName("Should get zero employees when provided not existing search query")
        public void should_getZeroEmployees_When_ProvidedNotExistingSearchQuery() {
            createManyEmployees();

            EmployeeReceivingRequest request = getEmployeeReceivingRequest("not-existing-search-query");

            Page<GetEmployeeResponse> response = employeeService.getAll(request);

            assertNotNull(request);
            assertEquals(10, employeeRepository.count());
            assertEquals(0, response.getTotalElements());
        }
    }
}
