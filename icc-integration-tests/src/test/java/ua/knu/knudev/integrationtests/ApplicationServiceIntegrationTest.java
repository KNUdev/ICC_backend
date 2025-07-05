package ua.knu.knudev.integrationtests;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;
import ua.knu.knudev.applicationmanager.domain.Application;
import ua.knu.knudev.applicationmanager.domain.Department;
import ua.knu.knudev.applicationmanager.repository.ApplicationRepository;
import ua.knu.knudev.applicationmanager.repository.DepartmentRepository;
import ua.knu.knudev.applicationmanager.service.ApplicationService;
import ua.knu.knudev.applicationmanagerapi.dto.ApplicationDto;
import ua.knu.knudev.applicationmanagerapi.exception.ApplicationException;
import ua.knu.knudev.applicationmanagerapi.request.*;
import ua.knu.knudev.employeemanager.domain.Employee;
import ua.knu.knudev.employeemanager.domain.Sector;
import ua.knu.knudev.employeemanager.domain.Specialty;
import ua.knu.knudev.employeemanager.domain.embeddable.WorkHours;
import ua.knu.knudev.employeemanager.repository.EmployeeRepository;
import ua.knu.knudev.employeemanager.repository.SectorRepository;
import ua.knu.knudev.employeemanager.repository.SpecialtyRepository;
import ua.knu.knudev.fileserviceapi.api.ImageServiceApi;
import ua.knu.knudev.fileserviceapi.subfolder.ImageSubfolder;
import ua.knu.knudev.icccommon.constant.EmployeeAdministrativeRole;
import ua.knu.knudev.icccommon.constant.SpecialtyCategory;
import ua.knu.knudev.icccommon.domain.embeddable.FullName;
import ua.knu.knudev.icccommon.domain.embeddable.MultiLanguageField;
import ua.knu.knudev.icccommon.dto.FullNameDto;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;
import ua.knu.knudev.icccommon.enums.ApplicationStatus;
import ua.knu.knudev.icccommon.mapper.FullNameMapper;
import ua.knu.knudev.integrationtests.config.IntegrationTestsConfig;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = IntegrationTestsConfig.class)
@ActiveProfiles("test")
public class ApplicationServiceIntegrationTest {

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

    public static final String TEST_APPLICANT_FIRST_NAME = "ApplicantFirstName";
    public static final String TEST_APPLICANT_MIDDLE_NAME = "ApplicantMiddleName";
    public static final String TEST_APPLICANT_LAST_NAME = "ApplicantLastName";
    public static final String TEST_APPLICANT_EMAIL = "applicant@knu.ua";
    public static final LocalDateTime TEST_APPLICATION_RECEIVED_AT = LocalDateTime.of(2019, 1, 1, 0, 0, 0);
    public static final LocalDateTime TEST_APPLICATION_COMPLETED_AT = LocalDateTime.of(2020, 1, 1, 0, 0);
    public static final String TEST_APPLICATION_PROBLEM_DESCRIPTION = "test-application-problem";
    public static final String TEST_APPLICATION_PROBLEM_PHOTO_NAME = "test-application-problem-photo.jpg";
    public static final MultipartFile TEST_APPLICATION_PHOTO_FILE = new MockMultipartFile("problem-photo.jpg", "problem-photo.jpeg", "image/jpeg", "image".getBytes());
    public static final ApplicationStatus TEST_APPLICATION_STATUS = ApplicationStatus.IN_WORK;

    public static final String TEST_DEPARTMENT_NAME_IN_ENGLISH = "test-department-name";
    public static final String TEST_DEPARTMENT_NAME_IN_UKRAINIAN = "тестове-ім'я-факультету";

    private final List<String> uploadedAvatarFiles = new ArrayList<>();
    private final List<String> uploadedProblemPhotoFiles = new ArrayList<>();

    @Autowired
    private FullNameMapper fullNameMapper;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private SectorRepository sectorRepository;
    @Autowired
    private SpecialtyRepository specialtyRepository;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private ImageServiceApi imageServiceApi;

    private Application testApplication;
    private Department testDepartment;
    private Employee testEmployee;
    private Sector testSector;
    private Specialty testSpecialty;

    @BeforeEach
    public void setUp() {
        testSector = createTestSector();
        testSpecialty = createTestSpecialty(testSector);
        testEmployee = createTestEmployee();
        testDepartment = createTestDepartment();
        testApplication = createTestApplication();
    }

    @AfterEach
    public void tearDown() {
        uploadedAvatarFiles.forEach(uploadedAvatarFile -> {
            imageServiceApi.removeByFilename(uploadedAvatarFile, ImageSubfolder.EMPLOYEE_AVATARS);
        });
        uploadedAvatarFiles.clear();
        applicationRepository.deleteAll();
        employeeRepository.deleteAll();
        sectorRepository.deleteAll();
        specialtyRepository.deleteAll();
        departmentRepository.deleteAll();
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

    private Department createTestDepartment() {
        Department department = new Department();
        department.setName(new MultiLanguageField(TEST_DEPARTMENT_NAME_IN_ENGLISH, TEST_DEPARTMENT_NAME_IN_UKRAINIAN));
        department.setCreatedAt(LocalDateTime.of(2015, 1, 1, 0, 0));
        department.setUpdatedAt(LocalDateTime.of(2022, 1, 1, 0, 0));
        department.setApplications(new HashSet<>());

        Department newDepartment = departmentRepository.save(department);
        return newDepartment;
    }

    private Application createTestApplication() {
        Application application = new Application();
        FullName fullName = new FullName(TEST_APPLICANT_FIRST_NAME, TEST_APPLICANT_MIDDLE_NAME, TEST_APPLICANT_LAST_NAME);

        String problemPhotoName = imageServiceApi.uploadFile(TEST_APPLICATION_PHOTO_FILE, TEST_APPLICATION_PROBLEM_PHOTO_NAME, ImageSubfolder.APPLICATIONS);

        application.setApplicantEmail(TEST_APPLICANT_EMAIL);
        application.setApplicantName(fullName);
        application.setDepartment(testDepartment);
        application.setProblemDescription(TEST_APPLICATION_PROBLEM_DESCRIPTION);
        application.setProblemPhoto(problemPhotoName);
        uploadedProblemPhotoFiles.add(problemPhotoName);
        application.setReceivedAt(TEST_APPLICATION_RECEIVED_AT);
        application.setCompletedAt(TEST_APPLICATION_COMPLETED_AT);
        application.setStatus(TEST_APPLICATION_STATUS);
        application.setAssignedEmployeeIds(new HashSet<>());

        Application newApplication = applicationRepository.save(application);
        testDepartment.getApplications().add(newApplication);
        departmentRepository.save(testDepartment);
        return newApplication;
    }

    private ApplicationCreateRequest createApplicationCreateRequest() {
        ApplicationCreateRequest request = ApplicationCreateRequest.builder()
                .applicantName(new FullNameDto(TEST_APPLICANT_FIRST_NAME, TEST_APPLICANT_MIDDLE_NAME, TEST_APPLICANT_LAST_NAME))
                .applicantEmail(TEST_APPLICANT_EMAIL)
                .status(TEST_APPLICATION_STATUS)
                .problemDescription(TEST_APPLICATION_PROBLEM_DESCRIPTION)
                .problemPhotoName(TEST_APPLICATION_PROBLEM_PHOTO_NAME)
                .problemPhoto(TEST_APPLICATION_PHOTO_FILE)
                .departmentId(testDepartment.getId())
                .build();

        return request;
    }

    private ApplicationCreateRequest createWrongApplicationCreateRequest() {
        ApplicationCreateRequest request = ApplicationCreateRequest.builder()
                .applicantName(new FullNameDto(TEST_APPLICANT_FIRST_NAME, TEST_APPLICANT_MIDDLE_NAME, TEST_APPLICANT_LAST_NAME))
                .applicantEmail(TEST_APPLICANT_EMAIL)
                .status(TEST_APPLICATION_STATUS)
                .problemDescription(TEST_APPLICATION_PROBLEM_DESCRIPTION)
                .problemPhotoName(TEST_APPLICATION_PROBLEM_PHOTO_NAME)
                .problemPhoto(TEST_APPLICATION_PHOTO_FILE)
                .departmentId(UUID.randomUUID())
                .build();

        return request;
    }

    @Nested
    @DisplayName("Get by id scenarios")
    class GetByIdScenarios {
        @Test
        @DisplayName("Should successfully get application when provided valid id")
        @Transactional
        public void should_SuccessfullyGet_When_ProvidedValidId() {
            ApplicationDto response = applicationService.getById(testApplication.getId());

            assertNotNull(response);
            assertEquals(testApplication.getApplicantEmail(), response.applicantEmail());
            assertEquals(testApplication.getApplicantName(), response.applicantName());
            assertEquals(testApplication.getDepartment().getId(), response.departmentId());
            assertEquals(testApplication.getStatus(), response.status());
            assertEquals(testApplication.getProblemDescription(), response.problemDescription());
            assertEquals(testApplication.getProblemPhoto(), response.problemPhoto());
            assertEquals(testApplication.getReceivedAt(), response.receivedAt());
        }

        @Test
        @DisplayName("Should throw ApplicationException when provided invalid id")
        @Transactional
        public void should_ThrowApplicationException_When_ProvidedInvalidId() {
            assertThrows(ApplicationException.class, () -> applicationService.getById(UUID.randomUUID()));
        }
    }

    @Nested
    @DisplayName("Create Application scenarios")
    class CreateApplicationScenarios {
        @Test
        @Transactional
        @DisplayName("Should successfully create application when provided valid data in request")
        public void should_SuccessfullyCreateApplication_When_ProvidedValidDataInRequest() {
            ApplicationCreateRequest request = createApplicationCreateRequest();
            ApplicationDto response = applicationService.create(request);

            assertNotNull(response);
            uploadedProblemPhotoFiles.add(response.problemPhoto());
            assertEquals(testApplication.getApplicantEmail(), response.applicantEmail());
            assertEquals(testApplication.getApplicantName(), response.applicantName());
            assertEquals(testApplication.getDepartment().getId(), response.departmentId());
            assertEquals(testApplication.getStatus(), response.status());
            assertEquals(testApplication.getProblemDescription(), response.problemDescription());
            assertEquals(testApplication.getProblemPhoto(), response.problemPhoto());
            assertTrue(applicationRepository.existsById(response.id()));
        }

        @Transactional
        @Test
        @DisplayName("Should throw IllegalArgumentException when provided non-existent department")
        public void should_ThrowIllegalArgumentException_When_ProvidedNonExistentDepartment() {
            ApplicationCreateRequest request = createWrongApplicationCreateRequest();

            assertThrows(IllegalArgumentException.class, () -> applicationService.create(request));
        }
    }

    @Nested
    @DisplayName("Update application scenarios")
    class UpdateApplicationScenarios {
        @Test
        @Transactional
        @DisplayName("Should successfully update application when provided valid data in request")
        public void should_SuccessfullyUpdateApplication_When_ProvidedValidDataInRequest() {
            ApplicationUpdateRequest request = new ApplicationUpdateRequest(
                    testApplication.getId(),
                    fullNameMapper.toDto(testApplication.getApplicantName()),
                    testApplication.getApplicantEmail(),
                    TEST_APPLICATION_COMPLETED_AT,
                    testApplication.getProblemDescription(),
                    TEST_APPLICATION_PROBLEM_PHOTO_NAME,
                    TEST_APPLICATION_PHOTO_FILE,
                    ApplicationStatus.DONE,
                    testApplication.getDepartment().getId());

            ApplicationDto response = applicationService.update(request);
            assertNotNull(response);
            assertEquals(testApplication.getStatus(), response.status());
            assertEquals(testApplication.getCompletedAt(), response.completedAt());
            assertTrue(applicationRepository.existsById(response.id()));
        }

        @Test
        @Transactional
        @DisplayName("Should throw IllegalArgumentException when provided invalid data")
        public void should_ThrowIllegalArgumentException_When_ProvidedInvalidData() {
            ApplicationUpdateRequest request = new ApplicationUpdateRequest(
                    testApplication.getId(),
                    fullNameMapper.toDto(testApplication.getApplicantName()),
                    testApplication.getApplicantEmail(),
                    TEST_APPLICATION_COMPLETED_AT,
                    testApplication.getProblemDescription(),
                    TEST_APPLICATION_PROBLEM_PHOTO_NAME,
                    TEST_APPLICATION_PHOTO_FILE,
                    ApplicationStatus.DONE,
                    UUID.randomUUID());

            assertThrows(IllegalArgumentException.class, () -> applicationService.update(request));
        }
    }

    @Nested
    @DisplayName("Get all scenarios")
    class GetAllScenarios {
        @Test
        @Transactional
        @DisplayName("Should successfully get all applications when provided appropriate request")
        public void should_SuccessfullyGetAllApplications_When_ProvidedAppropriateRequest() {
            for (int i = 0; i < 9; i++) {
                createTestApplication();
            }

            ApplicationGetAllRequest request = new ApplicationGetAllRequest(
                    TEST_APPLICANT_FIRST_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    new MultiLanguageFieldDto(TEST_DEPARTMENT_NAME_IN_ENGLISH, TEST_DEPARTMENT_NAME_IN_UKRAINIAN),
                    null,
                    1,
                    10);

            Page<ApplicationDto> response = applicationService.getAll(request);

            assertNotNull(response);
            assertEquals(10, applicationRepository.count());
            assertEquals(10, response.getTotalElements());
        }

        @Test
        @Transactional
        @DisplayName("Should get zero applications when provided not existing search query")
        public void should_ThrowApplicationException_When_ProvidedNotExistingSearchQuery() {
            for (int i = 0; i < 9; i++) {
                createTestApplication();
            }

            ApplicationGetAllRequest request = new ApplicationGetAllRequest(
                    "Something-wrong",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    new MultiLanguageFieldDto(TEST_DEPARTMENT_NAME_IN_ENGLISH, TEST_DEPARTMENT_NAME_IN_UKRAINIAN),
                    null,
                    1,
                    10);

            Page<ApplicationDto> response = applicationService.getAll(request);

            assertNotNull(response);
            assertEquals(10, applicationRepository.count());
            assertEquals(0, response.getTotalElements());
        }
    }

    @Nested
    @DisplayName("Add assigned employee scenarios")
    class AddAssignedEmployeeScenarios {
        @Test
        @Transactional
        @DisplayName("Should successfully add assigned employee to application")
        public void should_SuccessfullyAddAssignedEmployeeToApplication() {
            ApplicationAddAssignedEmployeeRequest request = new ApplicationAddAssignedEmployeeRequest(
                    testApplication.getId(),
                    testEmployee.getId());
            ApplicationDto response = applicationService.addAssignedEmployee(request);

            assertNotNull(response);
            assertEquals(1, testApplication.getAssignedEmployeeIds().size());
            assertTrue(testApplication.getAssignedEmployeeIds().contains(testEmployee.getId()));
        }

        @Test
        @Transactional
        @DisplayName("Should not to add assigned employee when provided not existing employee id")
        public void should_ThrowIllegalArgumentException_When_ProvidedNotExistingEmployeeId() {
            ApplicationAddAssignedEmployeeRequest request = new ApplicationAddAssignedEmployeeRequest(
                    testApplication.getId(),
                    UUID.randomUUID());

            assertThrows(ApplicationException.class, () -> applicationService.addAssignedEmployee(request));
        }
    }

    @Nested
    @DisplayName("Remove assigned employee scenarios")
    class RemoveAssignedEmployeeScenarios {
        @Test
        @Transactional
        @DisplayName("Should successfully remove assigned employee from application")
        public void should_SuccessfullyRemoveAssignedEmployeeFromApplication() {
            ApplicationAddAssignedEmployeeRequest addRequest = new ApplicationAddAssignedEmployeeRequest(
                    testApplication.getId(),
                    testEmployee.getId());
            applicationService.addAssignedEmployee(addRequest);

            ApplicationRemoveAssignedEmployeeRequest removeRequest = new ApplicationRemoveAssignedEmployeeRequest(
                    testApplication.getId(),
                    testEmployee.getId());
            ApplicationDto response = applicationService.removeAssignedEmployee(removeRequest);

            assertNotNull(response);
            assertEquals(0, testApplication.getAssignedEmployeeIds().size());
            assertFalse(testApplication.getAssignedEmployeeIds().contains(testEmployee.getId()));
        }

        @Test
        @Transactional
        @DisplayName("Should not remove assigned employee from application when provided non existing employee id")
        public void should_ThrowIllegalArgumentException_When_ProvidedNonExistingEmployeeId() {
            ApplicationAddAssignedEmployeeRequest addRequest = new ApplicationAddAssignedEmployeeRequest(
                    testApplication.getId(),
                    testEmployee.getId());
            applicationService.addAssignedEmployee(addRequest);

            ApplicationRemoveAssignedEmployeeRequest removeRequest = new ApplicationRemoveAssignedEmployeeRequest(
                    testApplication.getId(),
                    UUID.randomUUID());
            assertThrows(ApplicationException.class, () -> applicationService.removeAssignedEmployee(removeRequest));
        }
    }
}