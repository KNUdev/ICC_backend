package ua.knu.knudev.integrationtests;

import jakarta.validation.ConstraintViolationException;
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
import ua.knu.knudev.employeemanager.service.SectorService;
import ua.knu.knudev.employeemanagerapi.dto.SectorDto;
import ua.knu.knudev.employeemanagerapi.dto.SpecialtyDto;
import ua.knu.knudev.employeemanagerapi.exception.SectorException;
import ua.knu.knudev.employeemanagerapi.request.SectorCreationRequest;
import ua.knu.knudev.employeemanagerapi.request.SectorReceivingRequest;
import ua.knu.knudev.employeemanagerapi.request.SectorUpdateRequest;
import ua.knu.knudev.icccommon.constant.SpecialtyCategory;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;
import ua.knu.knudev.integrationtests.config.IntegrationTestsConfig;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = IntegrationTestsConfig.class)
@ActiveProfiles("test")
public class SectorServiceIntegrationTest {

    private static final String TEST_SECTOR_NAME_IN_ENGLISH = "test-sector-name";
    private static final String TEST_SECTOR_NAME_IN_UKRAINIAN = "тестове-ім'я-сектора";
    private static final UUID TEST_SECTOR_UUID = UUID.randomUUID();

    @Autowired
    private SectorService sectorService;
    @Autowired
    private SectorRepository sectorRepository;
    @Autowired
    private SpecialtyRepository specialtyRepository;
    @Autowired
    private SpecialtyMapper specialtyMapper;
    @Autowired
    private SectorMapper sectorMapper;
    @Autowired
    private MultiLanguageFieldMapper multiLanguageFieldMapper;

    private Sector testSector;
    private Specialty testSpecialty;

    @BeforeEach
    public void setUp() {
        testSpecialty = createTestSpecialty();
        testSector = createTestSector();
    }

    @AfterEach
    public void tearDown() {
        sectorRepository.deleteAll();
        specialtyRepository.deleteAll();
    }

    private Specialty createTestSpecialty() {
        Specialty specialty = new Specialty();
        specialty.setId(TEST_SECTOR_UUID);
        specialty.setName(new MultiLanguageField("Test Specialty for sectors", "Тестова спеціальність для секторів"));
        specialty.setCreatedAt(LocalDateTime.of(2019, 1, 1, 0, 0, 0));
        specialty.setUpdatedAt(LocalDateTime.of(2020, 1, 1, 0, 0));
        specialty.setCategory(SpecialtyCategory.FIRST);

        Specialty save = specialtyRepository.save(specialty);
        return save;
    }

    private Sector createTestSector() {
        Sector sector = new Sector();
        MultiLanguageField name = new MultiLanguageField(TEST_SECTOR_NAME_IN_ENGLISH, TEST_SECTOR_NAME_IN_UKRAINIAN);

        sector.setId(UUID.randomUUID());
        sector.setCreatedAt(LocalDateTime.of(2020, 1, 1, 0, 0));
        sector.setUpdatedAt(LocalDateTime.of(2022, 1, 1, 0, 0));
        sector.setName(name);
        sector.setSpecialties(Set.of(testSpecialty));
//        testSpecialty.getSectors().add(sector);
        Sector save = sectorRepository.save(sector);
        return save;
    }

    private SectorReceivingRequest createSectorReceivingRequest() {
        MultiLanguageFieldDto specialtyName = new MultiLanguageFieldDto("Test Specialty for sectors", "Тестова спеціальність для секторів");
        SectorReceivingRequest request = SectorReceivingRequest.builder()
                .searchQuery(TEST_SECTOR_NAME_IN_ENGLISH)
                .specialtyName(specialtyName)
                .createdBefore(LocalDateTime.of(2021, 1, 1, 0, 0))
                .createdAfter(LocalDateTime.of(2019, 1, 1, 0, 0))
                .updatedBefore(LocalDateTime.of(2023, 1, 1, 0, 0))
                .updatedAfter(LocalDateTime.of(2021, 1, 1, 0, 0))
                .pageNumber(0)
                .pageSize(9)
                .build();

        return request;
    }

    private SectorCreationRequest getSectorCreationRequest(String englishName, String ukrainianName) {
        SpecialtyDto specialtyDto = specialtyMapper.toDto(testSpecialty);
        MultiLanguageFieldDto sectorName = new MultiLanguageFieldDto(englishName, ukrainianName);
        SectorCreationRequest request = SectorCreationRequest.builder()
                .name(sectorName)
                .specialties(Set.of(specialtyDto))
                .build();

        return request;
    }

    @Nested
    @DisplayName("Get by id scenarios")
    class GetByIdScenarios {
        @Test
        @DisplayName("Should successfully get sector by id when provided valid id")
        void should_SuccessfullyGetSectorById_When_ProvidedValidId() {
            SectorDto sector = sectorService.getById(testSector.getId());

            assertNotNull(sector);
            assertEquals(testSector.getName().getEn(), sector.name().getEn());
            assertEquals(testSector.getSpecialties().size(), sector.specialties().size());
            assertEquals(testSector.getCreatedAt(), sector.createdAt());
            assertEquals(testSector.getUpdatedAt(), sector.updatedAt());
        }

        @Test
        @DisplayName("Should throw exception when provided not valid sector id")
        void should_ThrowException_When_ProvidedNotValidId() {
            assertThrows(SectorException.class, () -> sectorService.getById(UUID.randomUUID()));
        }
    }

    @Nested
    @DisplayName("Create Sector scenarios")
    class CreateScenarios {
        @Test
        @DisplayName("Should create sector successfully when provided valid account creation request")
        public void should_CreateSectorSuccessfully_When_ProvidedValidData() {
            SectorCreationRequest request = getSectorCreationRequest(TEST_SECTOR_NAME_IN_ENGLISH, TEST_SECTOR_NAME_IN_UKRAINIAN);
            SectorDto createdSector = sectorService.create(request);

            assertNotNull(createdSector);
            assertEquals(TEST_SECTOR_NAME_IN_ENGLISH, createdSector.name().getEn());
            assertEquals(TEST_SECTOR_NAME_IN_UKRAINIAN, createdSector.name().getUk());
            assertEquals(1, createdSector.specialties().size());

            assertTrue(sectorRepository.existsById(createdSector.id()));
        }

        @Test
        @DisplayName("Should throw ConstraintViolationException when creating with invalid name")
        void should_ThrowConstraintViolationException_When_ProvidedInvalidName() {
            SectorCreationRequest requestWithInvalidEnName = getSectorCreationRequest("Ім'я не англійською", TEST_SECTOR_NAME_IN_UKRAINIAN);
            SectorCreationRequest requestWithInvalidUkName = getSectorCreationRequest(TEST_SECTOR_NAME_IN_ENGLISH, "Not ukrainian name");
            assertThrows(ConstraintViolationException.class, () -> sectorService.create(requestWithInvalidEnName));
            assertThrows(ConstraintViolationException.class, () -> sectorService.create(requestWithInvalidUkName));
        }
    }

    @Nested
    @DisplayName("Update Sector scenarios")
    class UpdateScenarios {
        @Test
        @DisplayName("Should successfully update sector when provided valid request")
        void should_SuccessfullyUpdateSector_When_ProvidedValidRequest() {
            String newEnName = "new name";
            String newUkName = "нове ім'я";

            SectorUpdateRequest request = new SectorUpdateRequest(
                    testSector.getId(),
                    new MultiLanguageFieldDto(newEnName, newUkName),
                    Collections.emptySet()
            );
            SectorDto updatedSector = sectorService.update(request);

            assertNotNull(updatedSector);
            assertEquals(newEnName, updatedSector.name().getEn());
            assertEquals(0, updatedSector.specialties().size());

            Optional<Sector> sectorFromDb = sectorRepository.findById(updatedSector.id());

            assertTrue(sectorFromDb.isPresent());
            assertEquals(newUkName, sectorFromDb.get().getName().getUk());
        }

        @Test
        @DisplayName("Should throw exception when provided invalid english name")
        void should_ThrowConstraintViolationException_When_ProvidedInvalidEnglishName() {
            String invalidEnName = "Ім'я не англійською";
            String newUkName = "нове ім'я";

            SectorUpdateRequest request = new SectorUpdateRequest(
                    testSector.getId(),
                    new MultiLanguageFieldDto(invalidEnName, newUkName),
                    Collections.emptySet()
            );
            assertThrows(SectorException.class, () -> sectorService.update(request));
        }
    }

    @Test
    @DisplayName("Should successfully delete sector when provided existing id")
    void should_SuccessfullyDeleteSector_When_ProvidedExistingId() {
        sectorRepository.deleteById(testSector.getId());

        assertFalse(sectorRepository.existsById(testSector.getId()));
    }

    @Test
    @DisplayName("Should successfully get sectors when provided valid data")
    void should_SuccessfullyGetAllSectors_When_ProvidedValidData() {
        for (int i = 0; i < 9; i++) {
            Sector sector = new Sector();
            MultiLanguageField name = new MultiLanguageField(TEST_SECTOR_NAME_IN_ENGLISH, TEST_SECTOR_NAME_IN_UKRAINIAN);

            sector.setId(UUID.randomUUID());
            sector.setCreatedAt(LocalDateTime.of(2020, 1, 1, 0, 0));
            sector.setUpdatedAt(LocalDateTime.of(2022, 1, 1, 0, 0));
            sector.setName(name);
            sector.setSpecialties(Set.of(testSpecialty));
            Sector save = sectorRepository.save(sector);
        }

        SectorReceivingRequest sectorReceivingRequest = createSectorReceivingRequest();
        Page<SectorDto> sectors = sectorService.getAll(sectorReceivingRequest);
        SectorDto firstSector = sectors.get().findFirst().get();

        assertNotNull(sectors);
        assertEquals(10, sectors.getTotalElements());
        assertEquals(testSector.getName().getEn(), firstSector.name().getEn());
        assertEquals(testSector.getName().getUk(), firstSector.name().getUk());
        assertEquals(testSector.getSpecialties().size(), firstSector.specialties().size());
        assertEquals(testSector.getCreatedAt(), firstSector.createdAt());
        assertEquals(testSector.getUpdatedAt(), firstSector.updatedAt());
    }

}
