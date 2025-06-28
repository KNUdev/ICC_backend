package ua.knu.knudev.integrationtests;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;
import ua.knu.knudev.applicationmanager.domain.Application;
import ua.knu.knudev.applicationmanager.domain.Department;
import ua.knu.knudev.applicationmanager.mapper.ApplicationMapper;
import ua.knu.knudev.applicationmanager.mapper.DepartmentMapper;
import ua.knu.knudev.applicationmanager.repository.ApplicationRepository;
import ua.knu.knudev.applicationmanager.repository.DepartmentRepository;
import ua.knu.knudev.applicationmanager.service.ApplicationService;
import ua.knu.knudev.applicationmanager.service.DepartmentService;
import ua.knu.knudev.employeemanager.domain.Employee;
import ua.knu.knudev.employeemanager.domain.Sector;
import ua.knu.knudev.employeemanager.domain.Specialty;
import ua.knu.knudev.employeemanager.domain.embeddable.WorkHours;
import ua.knu.knudev.employeemanager.mapper.SectorMapper;
import ua.knu.knudev.employeemanager.mapper.SpecialtyMapper;
import ua.knu.knudev.employeemanager.repository.EmployeeRepository;
import ua.knu.knudev.employeemanager.repository.SectorRepository;
import ua.knu.knudev.employeemanager.repository.SpecialtyRepository;
import ua.knu.knudev.fileserviceapi.api.ImageServiceApi;
import ua.knu.knudev.fileserviceapi.subfolder.ImageSubfolder;
import ua.knu.knudev.icccommon.constant.EmployeeAdministrativeRole;
import ua.knu.knudev.icccommon.constant.SpecialtyCategory;
import ua.knu.knudev.icccommon.domain.embeddable.FullName;
import ua.knu.knudev.icccommon.domain.embeddable.MultiLanguageField;
import ua.knu.knudev.icccommon.enums.ApplicationStatus;
import ua.knu.knudev.icccommon.mapper.FullNameMapper;
import ua.knu.knudev.icccommon.mapper.MultiLanguageFieldMapper;
import ua.knu.knudev.integrationtests.config.IntegrationTestsConfig;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    public static final String TEST_APPLICATION_PROBLEM_PHOTO_NAME = "test-application-problem-photo";
    public static final MultipartFile TEST_APPLICATION_PHOTO_FILE = new MockMultipartFile("problem-photo.jpg", "problem-photo.jpeg", "image/jpeg", "image".getBytes());
    public static final ApplicationStatus TEST_APPLICATION_STATUS = ApplicationStatus.IN_WORK;

    public static final String TEST_DEPARTMENT_NAME_IN_ENGLISH = "test-department-name";
    public static final String TEST_DEPARTMENT_NAME_IN_UKRAINIAN = "тестове-ім'я-факультету";

    private final List<String> uploadedAvatarFiles = new ArrayList<>();
    private final List<String> uploadedProblemPhotoFiles = new ArrayList<>();

    @Autowired
    private FullNameMapper fullNameMapper;
    @Autowired
    private MultiLanguageFieldMapper multiLanguageFieldMapper;
    @Autowired
    private ApplicationMapper applicationMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private SpecialtyMapper specialtyMapper;
    @Autowired
    private SectorMapper sectorMapper;
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
    private DepartmentService departmentService;
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
        testApplication = createTestApplication();
        testDepartment = createTestDepartment();
    }

    @AfterEach
    public void tearDown() {
        uploadedAvatarFiles.forEach(uploadedAvatarFile -> {
            imageServiceApi.removeByFilename(uploadedAvatarFile, ImageSubfolder.EMPLOYEE_AVATARS);
        });
        uploadedAvatarFiles.clear();
        uploadedProblemPhotoFiles.forEach(uploadedProblemPhotoFile ->
                imageServiceApi.removeByFilename(uploadedProblemPhotoFile, ImageSubfolder.APPLICATION));
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

        String problemPhotoName = imageServiceApi.uploadFile(TEST_APPLICATION_PHOTO_FILE, TEST_APPLICATION_PROBLEM_PHOTO_NAME, ImageSubfolder.APPLICATION);

        application.setApplicantEmail(TEST_APPLICANT_EMAIL);
        application.setApplicantName(fullName);
        application.setDepartment(testDepartment);
        application.setProblemDescription(TEST_APPLICATION_PROBLEM_DESCRIPTION);
        application.setProblemPhoto(problemPhotoName);
        uploadedProblemPhotoFiles.add(problemPhotoName);
        application.setReceivedAt(TEST_APPLICATION_RECEIVED_AT);
        application.setStatus(TEST_APPLICATION_STATUS);

        Application newApplication = applicationRepository.save(application);
        return newApplication;
    }

    @Nested
    @DisplayName("Get by id scenarios")
    class GetByIdScenarios {
        @Test
        @DisplayName("Should successfully get application when provided valid id")
        @Transactional
        public void should_SuccessfullyGet_When_ProvidedValidId() {
            Application response = applicationMapper.toDomain(applicationService.getById(testApplication.getId()));

            assertNotNull(response);
            assertEquals(testApplication.getApplicantEmail(), response.getApplicantEmail());
            assertEquals(testApplication.getApplicantName(), response.getApplicantName());
            assertEquals(testApplication.getDepartment().getName(), response.getDepartment().getName());
            assertEquals(testApplication.getStatus(), response.getStatus());
            assertEquals(testApplication.getProblemDescription(), response.getProblemDescription());
            assertEquals(testApplication.getProblemPhoto(), response.getProblemPhoto());
            assertEquals(testApplication.getReceivedAt(), response.getReceivedAt());
        }
    }
}
