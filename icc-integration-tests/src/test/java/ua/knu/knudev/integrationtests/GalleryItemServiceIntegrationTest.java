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
import ua.knu.knudev.employeemanager.domain.Employee;
import ua.knu.knudev.employeemanager.domain.Sector;
import ua.knu.knudev.employeemanager.domain.Specialty;
import ua.knu.knudev.icccommon.domain.embeddable.FullName;
import ua.knu.knudev.employeemanager.domain.embeddable.WorkHours;
import ua.knu.knudev.employeemanager.repository.EmployeeRepository;
import ua.knu.knudev.employeemanager.repository.SectorRepository;
import ua.knu.knudev.employeemanager.repository.SpecialtyRepository;
import ua.knu.knudev.fileservice.domain.GalleryItem;
import ua.knu.knudev.fileservice.repository.GalleryItemRepository;
import ua.knu.knudev.fileservice.service.GalleryItemService;
import ua.knu.knudev.fileserviceapi.api.ImageServiceApi;
import ua.knu.knudev.fileserviceapi.dto.GalleryItemDto;
import ua.knu.knudev.fileserviceapi.exception.GalleryItemException;
import ua.knu.knudev.fileserviceapi.request.GalleryItemUpdateRequest;
import ua.knu.knudev.fileserviceapi.request.GalleryItemUploadRequest;
import ua.knu.knudev.fileserviceapi.subfolder.ImageSubfolder;
import ua.knu.knudev.icccommon.constant.EmployeeAdministrativeRole;
import ua.knu.knudev.icccommon.constant.SpecialtyCategory;
import ua.knu.knudev.icccommon.domain.embeddable.MultiLanguageField;
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
public class GalleryItemServiceIntegrationTest {

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

    public static final String TEST_GALLERY_ITEM_NAME = "test-gallery-item-name";
    public static final String TEST_GALLERY_ITEM_DESCRIPTION = "test-gallery-item-description";
    public static final MultipartFile TEST_GALLERY_ITEM_FILE = new MockMultipartFile("test1.jpg", "test1.jpeg", "image/jpeg", "image".getBytes());
    public static final LocalDateTime TEST_GALLERY_ITEM_UPLOADED_AT = LocalDateTime.of(2019, 1, 1, 0, 0, 0);
    public static final LocalDateTime TEST_GALLERY_ITEM_UPDATED_AT = LocalDateTime.of(2020, 1, 1, 0, 0);

    private final List<String> uploadedAvatarFiles = new ArrayList<>();
    private final List<String> uploadedGalleryItems = new ArrayList<>();

    @Autowired
    private GalleryItemService galleryItemService;
    @Autowired
    private ImageServiceApi imageServiceApi;
    @Autowired
    private GalleryItemRepository galleryItemRepository;
    @Autowired
    private SectorRepository sectorRepository;
    @Autowired
    private SpecialtyRepository specialtyRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    private GalleryItem testGalleryItem;
    private Sector testSector;
    private Specialty testSpecialty;
    private Employee testEmployee;

    @BeforeEach
    public void setUp() {
        testSector = createTestSector();
        testSpecialty = createTestSpecialty(testSector);
        testEmployee = createTestEmployee();
        testGalleryItem = createTestGalleryItem();
    }

    @AfterEach
    public void tearDown() {
        uploadedAvatarFiles.forEach(uploadedAvatarFile -> {
            imageServiceApi.removeByFilename(uploadedAvatarFile, ImageSubfolder.EMPLOYEE_AVATARS);
        });
        uploadedAvatarFiles.clear();
        uploadedGalleryItems.forEach(uploadedGalleryItem -> {
            imageServiceApi.removeByFilename(uploadedGalleryItem, ImageSubfolder.GALLERY);});
        uploadedGalleryItems.clear();
        employeeRepository.deleteAll();
        sectorRepository.deleteAll();
        specialtyRepository.deleteAll();
        galleryItemRepository.deleteAll();
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

    private GalleryItem createTestGalleryItem() {
        GalleryItem galleryItem = new GalleryItem();

        String itemName = imageServiceApi.uploadFile(TEST_GALLERY_ITEM_FILE, TEST_GALLERY_ITEM_NAME + "_" + UUID.randomUUID() + ".jpg", ImageSubfolder.GALLERY);

        galleryItem.setCreatorId(testEmployee.getId());
        galleryItem.setItemName(itemName);
        uploadedGalleryItems.add(itemName);
        galleryItem.setPublicItemName(itemName + "_" + UUID.randomUUID());
        galleryItem.setItemDescription(TEST_GALLERY_ITEM_DESCRIPTION);
        galleryItem.setUploadedAt(TEST_GALLERY_ITEM_UPLOADED_AT);
        galleryItem.setUpdatedAt(TEST_GALLERY_ITEM_UPDATED_AT);

        GalleryItem newGalleryItem = galleryItemRepository.save(galleryItem);
        return newGalleryItem;
    }

    private GalleryItemUploadRequest createTestGalleryItemUploadRequest() {
        return GalleryItemUploadRequest.builder()
                .creatorId(testEmployee.getId())
                .item(TEST_GALLERY_ITEM_FILE)
                .itemDescription(TEST_GALLERY_ITEM_DESCRIPTION)
                .build();
    }

    private void createManyGalleryItems() {
        for(int i = 0; i < 9; i++) {
            createTestGalleryItem();
        }
    }

    @Nested
    @DisplayName("Get by id scenarios")
    class GetByIdScenarios {
        @Test
        @DisplayName("Should successfully get GalleryItem when provided valid id")
        @Transactional
        public void should_SuccessfullyGet_When_ProvidedValidId() {
            GalleryItem response = galleryItemService.getGalleryItemById(testGalleryItem.getItemId());

            assertNotNull(response);
            assertEquals(testGalleryItem.getItemName(), response.getItemName());
            assertEquals(testGalleryItem.getItemDescription(), response.getItemDescription());
            assertEquals(testGalleryItem.getUploadedAt(), response.getUploadedAt());
            assertEquals(testGalleryItem.getUpdatedAt(), response.getUpdatedAt());
            assertEquals(testGalleryItem.getCreatorId(), response.getCreatorId());
        }

        @Test
        @DisplayName("Should throw GalleryItemException when provided invalid id")
        public void should_ThrowGalleryItemException_When_ProvidedInvalidId() {
            assertThrows(GalleryItemException.class, () -> galleryItemService.getById(UUID.randomUUID()));
        }
    }

    @Test
    @DisplayName("Should successfully upload gallery item when provided valid data in request")
    public void should_SuccessfullyUpload_When_ProvidedValidDataInRequest() {
        GalleryItemUploadRequest request = createTestGalleryItemUploadRequest();
        GalleryItemDto response = galleryItemService.upload(request);

        assertNotNull(response);
        uploadedGalleryItems.add(response.itemName());
        assertEquals(testEmployee.getId(), response.creatorId());
        assertEquals(TEST_GALLERY_ITEM_FILE.getName(), response.itemName());
        assertEquals(TEST_GALLERY_ITEM_DESCRIPTION, response.itemDescription());
        assertTrue(galleryItemRepository.existsById(response.itemId()));
    }

    @Test
    @DisplayName("Should successfully update gallery item when provided valid data in request")
    public void should_SuccessfullyUpdate_When_ProvidedValidDataInRequest() {
        GalleryItemUpdateRequest request = new GalleryItemUpdateRequest(
               TEST_GALLERY_ITEM_FILE,
                testGalleryItem.getItemId(),
                testGalleryItem.getItemName(),
                testGalleryItem.getItemDescription());

        GalleryItemDto response = galleryItemService.update(request);
        assertNotNull(response);
        uploadedGalleryItems.removeLast();
        uploadedGalleryItems.add(response.itemName());
        assertEquals(testGalleryItem.getItemName(), response.itemName());
        assertEquals(testGalleryItem.getItemDescription(), response.itemDescription());
        assertEquals(testGalleryItem.getItemId(), response.itemId());
        assertEquals(testGalleryItem.getUploadedAt(), response.uploadedAt());
        assertTrue(galleryItemRepository.existsById(response.itemId()));
    }

    @Test
    @DisplayName("Should successfully get all gallery items when provided appropriate request")
    public void should_SuccessfullyGetAll_When_ProvidedAppropriateRequest() {
        createManyGalleryItems();

        Page<GalleryItemDto> response = galleryItemService.getAll(1, 10);
        assertNotNull(response);
        assertEquals(10, response.getTotalElements());
        assertEquals(10, galleryItemRepository.count());
    }
}