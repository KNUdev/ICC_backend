package ua.knu.knudev.integrationtests;

import jakarta.validation.ConstraintViolationException;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import ua.knu.knudev.employeemanager.domain.Sector;
import ua.knu.knudev.employeemanager.domain.Specialty;
import ua.knu.knudev.employeemanager.domain.embeddable.MultiLanguageField;
import ua.knu.knudev.employeemanager.mapper.MultiLanguageFieldMapper;
import ua.knu.knudev.employeemanager.mapper.SectorMapper;
import ua.knu.knudev.employeemanager.mapper.SpecialtyMapper;
import ua.knu.knudev.employeemanager.repository.SectorRepository;
import ua.knu.knudev.employeemanager.repository.SpecialtyRepository;
import ua.knu.knudev.employeemanager.service.SpecialtyService;
import ua.knu.knudev.employeemanagerapi.dto.SpecialtyDto;
import ua.knu.knudev.employeemanagerapi.exception.SpecialtyException;
import ua.knu.knudev.employeemanagerapi.request.SpecialtyCreationRequest;
import ua.knu.knudev.employeemanagerapi.request.SpecialtyReceivingRequest;
import ua.knu.knudev.employeemanagerapi.request.SpecialtyUpdateRequest;
import ua.knu.knudev.icccommon.constant.SpecialtyCategory;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;
import ua.knu.knudev.integrationtests.config.IntegrationTestsConfig;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = {IntegrationTestsConfig.class})
@ActiveProfiles("test")
public class SpecialtyServiceIntegrationTest {

    private static final String TEST_SPECIALTY_NAME_IN_ENGLISH = "test-specialty-name";
    private static final String TEST_SPECIALTY_NAME_IN_UKRAINIAN = "тестове-імя-спеціальності";
    private static final UUID TEST_SPECIALTY_UUID = UUID.randomUUID();
    private static final UUID TEST_SECTOR_UUID = UUID.randomUUID();

    @Autowired
    private SpecialtyService specialtyService;
    @Autowired
    private SpecialtyRepository specialtyRepository;
    @Autowired
    private SectorRepository sectorRepository;
    @Autowired
    private SectorMapper sectorMapper;
    @Autowired
    private SpecialtyMapper specialtyMapper;
    @Autowired
    private MultiLanguageFieldMapper multiLanguageFieldMapper;

    private Specialty testSpecialty;
    private Sector testSector;

    @BeforeEach
    public void setUp() {
        testSector = createTestSector();
        testSpecialty = createTestSpecialty();
    }

    @AfterEach
    public void tearDown() {
        sectorRepository.deleteAll();
        specialtyRepository.deleteAll();
    }

    private Sector createTestSector(Specialty... specialties) {
        Sector sector = new Sector();
        sector.setId(TEST_SECTOR_UUID);
        sector.setName(new MultiLanguageField("Test Sector for specialties", "Тестовий сектор для спеціальностей"));
        sector.setCreatedAt(LocalDateTime.of(2019, 1, 1, 0, 0));
        sector.setUpdatedAt(LocalDateTime.of(2020, 1, 1, 0, 0));

        Set<Specialty> specialtiesSet = new HashSet<>(Arrays.asList(specialties));
        sector.setSpecialties(specialtiesSet);

        return sectorRepository.save(sector);
    }

    public Specialty createTestSpecialty() {
        Specialty specialty = new Specialty();
        specialty.setId(TEST_SPECIALTY_UUID);
        specialty.setName(new MultiLanguageField(TEST_SPECIALTY_NAME_IN_ENGLISH, TEST_SPECIALTY_NAME_IN_UKRAINIAN));
        specialty.setCreatedAt(LocalDateTime.of(2020, 1, 1, 0, 0));
        specialty.setUpdatedAt(LocalDateTime.of(2022, 1, 1, 0, 0));
        specialty.setCategory(SpecialtyCategory.SENIOR);
        specialty.setSectors(new HashSet<>());

        specialty = specialtyRepository.save(specialty);
        testSector.addSpecialty(specialty);

        testSector = sectorRepository.save(testSector);

        return specialty;
    }

    private void createManySpecialties() {
        for (int i = 0; i < 9; i++) {
            Specialty specialty = new Specialty();
            specialty.setId(UUID.randomUUID());
            specialty.setName(new MultiLanguageField(TEST_SPECIALTY_NAME_IN_ENGLISH, TEST_SPECIALTY_NAME_IN_UKRAINIAN));
            specialty.setCreatedAt(LocalDateTime.of(2020, 1, 1, 0, 0));
            specialty.setUpdatedAt(LocalDateTime.of(2022, 1, 1, 0, 0));
            specialty.setCategory(SpecialtyCategory.SENIOR);
            specialty.setSectors(new HashSet<>());

            specialty = specialtyRepository.save(specialty);
            testSector.addSpecialty(specialty);
        }

        testSector = sectorRepository.save(testSector);
    }

    private SpecialtyReceivingRequest createSpecialtyReceivingRequest() {
        return SpecialtyReceivingRequest.builder()
                .searchQuery(TEST_SPECIALTY_NAME_IN_ENGLISH)
                .category(SpecialtyCategory.SENIOR)
                .sectorName(new MultiLanguageFieldDto(testSector.getName().getEn(), testSector.getName().getUk()))
                .createdBefore(LocalDateTime.of(2021,1,1,0,0))
                .createdAfter(LocalDateTime.of(2019,1,1,0,0))
                .updatedBefore(LocalDateTime.of(2023,1,1,0,0))
                .updatedAfter(LocalDateTime.of(2021,1,1,0,0))
                .pageNumber(0)
                .pageSize(10)
                .build();
    }

    private SpecialtyReceivingRequest createWrongSpecialtyReceivingRequest() {
        return SpecialtyReceivingRequest.builder()
                .searchQuery("not-existing-search-query")
                .category(SpecialtyCategory.SENIOR)
                .sectorName(new MultiLanguageFieldDto(testSector.getName().getEn(), testSector.getName().getUk()))
                .createdBefore(LocalDateTime.of(2021,1,1,0,0))
                .createdAfter(LocalDateTime.of(2019,1,1,0,0))
                .updatedBefore(LocalDateTime.of(2023,1,1,0,0))
                .updatedAfter(LocalDateTime.of(2021,1,1,0,0))
                .pageNumber(0)
                .pageSize(10)
                .build();
    }

    @Transactional
    @Nested
    @DisplayName("Create Specialty scenarios")
    class CreateSpecialtyScenarios {
        @Test
        @DisplayName("Should create Specialty successfully when provided valid data")
        public void should_CreateSpecialtySuccessfully_When_ProvidedValidData() {

            SpecialtyCreationRequest request = new SpecialtyCreationRequest(
                    new MultiLanguageFieldDto("Test-Specialty", "Тестова-Спеціальність"),
                    SpecialtyCategory.SENIOR,
                    Set.of(sectorMapper.toDto(testSector))
            );

            SpecialtyDto response = specialtyService.create(request);

            Sector sector = sectorRepository.findById(response.sectors().iterator().next().id()).orElse(null);
            assertEquals(2, sector.getSpecialties().size());
            assertNotNull(response);
            assertEquals("Test-Specialty", response.name().getEn());
            assertEquals(testSpecialty.getSectors().size(), response.sectors().size());
            assertEquals(SpecialtyCategory.SENIOR, response.category());

            Optional<Specialty> specialtyFromDb = specialtyRepository.findById(UUID.fromString(String.valueOf(response.id())));
            assertTrue(specialtyFromDb.isPresent());
            assertEquals("Тестова-Спеціальність", specialtyFromDb.get().getName().getUk());
        }

        @Test
        @DisplayName("Should throw ConstraintViolationException when creating with invalid name")
        void should_ThrowConstraintViolationException_When_ProvidedInvalidName() {
            SpecialtyCreationRequest requestWithInvalidEnName = new SpecialtyCreationRequest(
                    new MultiLanguageFieldDto("Неправильне імя", "Тестова-Спеціальність"),
                    SpecialtyCategory.SENIOR,
                    Set.of(sectorMapper.toDto(testSector))
            );
            SpecialtyCreationRequest requestWithInvalidUkName = new SpecialtyCreationRequest(
                    new MultiLanguageFieldDto("Test-Specialty", "Wrong name"),
                    SpecialtyCategory.SENIOR,
                    Set.of(sectorMapper.toDto(testSector))
            );

            assertThrows(ConstraintViolationException.class, () -> specialtyService.create(requestWithInvalidEnName));
            assertThrows(ConstraintViolationException.class, () -> specialtyService.create(requestWithInvalidUkName));
        }
    }

    @Nested
    @DisplayName("Update Specialty scenarios")
    class UpdateSpecialtyScenarios {
        @Test
        @DisplayName("Should update Specialty successfully when provided valid data")
        public void should_UpdateExistingSpecialty_When_ProvidedValidData() {
            Specialty specialty = testSpecialty;

            SpecialtyUpdateRequest request = new SpecialtyUpdateRequest(
                    specialty.getId(), multiLanguageFieldMapper.toDto(specialty.getName()),
                    SpecialtyCategory.LEAD, Set.of(sectorMapper.toDto(testSector))
            );

            SpecialtyDto response = specialtyService.update(request);

            assertNotNull(response);
            assertEquals(TEST_SPECIALTY_NAME_IN_ENGLISH, response.name().getEn());
            assertEquals(testSpecialty.getSectors().size(), response.sectors().size());
            assertEquals(SpecialtyCategory.LEAD, response.category());

            Optional<Specialty> specialtyFromDb = specialtyRepository.findById(UUID.fromString(String.valueOf(response.id())));
            assertTrue(specialtyFromDb.isPresent());
            assertEquals(TEST_SPECIALTY_NAME_IN_UKRAINIAN, specialtyFromDb.get().getName().getUk());
        }

        @Test
        @DisplayName("Should throw exception when provided invalid english name")
        void should_ThrowConstraintViolationException_When_ProvidedInvalidEnglishName() {
            String invalidEnName = "Ім'я не англійською";
            String newUkName = "нове ім'я";

            SpecialtyUpdateRequest request = new SpecialtyUpdateRequest(
                    testSector.getId(), new MultiLanguageFieldDto(invalidEnName, newUkName),
                    SpecialtyCategory.LEAD, Set.of(sectorMapper.toDto(testSector)));
            assertThrows(SpecialtyException.class, () -> specialtyService.update(request));
        }
    }

    @Nested
    @DisplayName("Get by id scenarios")
    @Transactional
    class GetByIdScenarios {
        @Test
        @DisplayName("Should successfully get Specialty by id when provided valid id")
        public void should_SuccessfullyGetSpecialtyById_When_ProvidedValidId() {
            SpecialtyDto response = specialtyService.getById(testSpecialty.getId());

            assertNotNull(response);
            assertEquals(testSpecialty.getName().getEn(), response.name().getEn());
            assertEquals(testSpecialty.getSectors().size(), response.sectors().size());
            assertEquals(testSpecialty.getCategory(), response.category());
            assertEquals(testSpecialty.getCreatedAt(), response.createdAt());
            assertEquals(testSpecialty.getUpdatedAt(), response.updatedAt());
        }

        @Test
        @DisplayName("Should throw exception when provided not valid Specialty id")
        void should_ThrowException_When_ProvidedInvalidId() {
            assertThrows(SpecialtyException.class, () -> specialtyService.getById(UUID.randomUUID()));
        }
    }

    @Nested
    @DisplayName("Get all scenarios")
    @Transactional
    class GetAllScenarios {
        @Test
        @DisplayName("Should successfully get all Specialties by filter when provided valid data")
        public void should_SuccessfullyGetAllSpecialties_When_ProvidedValidData() {
            createManySpecialties();
            SpecialtyReceivingRequest specialtyReceivingRequest = createSpecialtyReceivingRequest();

            Page<SpecialtyDto> response = specialtyService.getAll(specialtyReceivingRequest);
            SpecialtyDto firstResponseDto = response.get().findFirst().get();

            assertNotNull(response);
            assertEquals(10, response.getTotalElements());
            assertEquals(testSpecialty.getName().getEn(), firstResponseDto.name().getEn());
            assertEquals(testSpecialty.getName().getUk(), firstResponseDto.name().getUk());
            assertEquals(testSpecialty.getCategory(), firstResponseDto.category());
            assertEquals(testSpecialty.getCreatedAt(), firstResponseDto.createdAt());
            assertEquals(testSpecialty.getUpdatedAt(), firstResponseDto.updatedAt());
            assertEquals(testSpecialty.getSectors().size(), firstResponseDto.sectors().size());
        }

        @Test
        @DisplayName("Should get zero specialties when provided invalid search query")
        void should_ThrowException_When_ProvidedInvalidSearchQuery() {
            createManySpecialties();
            SpecialtyReceivingRequest specialtyReceivingRequest = createWrongSpecialtyReceivingRequest();

            Page<SpecialtyDto> response = specialtyService.getAll(specialtyReceivingRequest);
            assertEquals(0, response.getTotalElements());
        }
    }
}
