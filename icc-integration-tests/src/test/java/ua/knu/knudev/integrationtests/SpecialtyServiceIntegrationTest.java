package ua.knu.knudev.integrationtests;

import jakarta.transaction.Transactional;
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
import ua.knu.knudev.employeemanagerapi.dto.ShortSpecialtyDto;
import ua.knu.knudev.employeemanagerapi.dto.SpecialtyDto;
import ua.knu.knudev.employeemanagerapi.exception.SpecialtyException;
import ua.knu.knudev.employeemanagerapi.request.SpecialtyCreationRequest;
import ua.knu.knudev.employeemanagerapi.request.SpecialtyReceivingRequest;
import ua.knu.knudev.employeemanagerapi.request.SpecialtyUpdateRequest;
import ua.knu.knudev.icccommon.constant.SpecialtyCategory;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;
import ua.knu.knudev.integrationtests.config.IntegrationTestsConfig;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = {IntegrationTestsConfig.class})
@ActiveProfiles("test")
public class SpecialtyServiceIntegrationTest {

    private static final String TEST_SPECIALTY_NAME_IN_ENGLISH = "test-specialty-name";
    private static final String TEST_SPECIALTY_NAME_IN_UKRAINIAN = "тестове-ім'я-спеціальності";
    private static final UUID TEST_SPECIALTY_UUID = UUID.randomUUID();

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

    private Sector createTestSector() {
        Sector sector = new Sector();
        sector.setId(UUID.randomUUID());
        sector.setName(new MultiLanguageField("Test Sector for specialties", "Тестовий сектор для спеціальностей"));
        sector.setCreatedAt(LocalDateTime.of(2020, 1, 1, 0, 0));
        sector.setUpdatedAt(LocalDateTime.of(2022, 1, 1, 0, 0));

        return sectorRepository.save(sector);
    }

    public Specialty createTestSpecialty() {
        Specialty specialty = new Specialty();
        specialty.setId(TEST_SPECIALTY_UUID);
        specialty.setName(new MultiLanguageField(TEST_SPECIALTY_NAME_IN_ENGLISH, TEST_SPECIALTY_NAME_IN_UKRAINIAN));
        specialty.setCreatedAt(LocalDateTime.of(2020, 1, 1, 0, 0));
        specialty.setUpdatedAt(LocalDateTime.of(2022, 1, 1, 0, 0));
        specialty.setCategory(SpecialtyCategory.SENIOR);
        specialty.setSectors(Set.of(testSector));
        testSector.addSpecialty(specialty);

        sectorRepository.save(testSector);
        return specialtyRepository.save(specialty);
    }

    @Test
    public void testCreateSector() {
        System.out.println("testCreateSector");
    }

    @Test
    public void testCreateSpecialty() {
        System.out.println("testCreateSpecialty");
    }

    @Test
    @DisplayName("Should create Specialty successfully")
    public void should_CreateSpecialtySuccessfully() {
        Sector sector = testSector;

        SpecialtyCreationRequest request = new SpecialtyCreationRequest(
                new MultiLanguageFieldDto("Test-Specialty", "Тестова-Спеціальність"),
                SpecialtyCategory.SENIOR,
                Set.of(sectorMapper.toDto(sector))
        );

        SpecialtyDto created = specialtyService.create(request);

        assertNotNull(created);
        assertEquals("Test-Specialty", created.name().getEn());
        assertEquals(testSpecialty.getSectors().size(), created.sectors().size());
        assertEquals(SpecialtyCategory.SENIOR, created.category());

        Optional<Specialty> fromDb = specialtyRepository.findById(UUID.fromString(String.valueOf(created.id())));
        assertTrue(fromDb.isPresent());
        assertEquals("Тестова-Спеціальність", fromDb.get().getName().getUk());
    }

    @Test
    @DisplayName("Should update Specialty successfully")
    public void should_UpdateExistingSpecialty() {
        Sector sector = testSector;
        Specialty specialty = testSpecialty;

        SpecialtyUpdateRequest updatedRequest = new SpecialtyUpdateRequest(
                specialty.getId(), multiLanguageFieldMapper.toDto(specialty.getName()),
                SpecialtyCategory.LEAD, Set.of(sectorMapper.toDto(sector))
        );

        SpecialtyDto updated = specialtyService.update(updatedRequest);

        assertNotNull(updated);
        assertEquals(TEST_SPECIALTY_NAME_IN_ENGLISH, updated.name().getEn());
        assertEquals(testSpecialty.getSectors().size(), updated.sectors().size());
        assertEquals(SpecialtyCategory.LEAD, updated.category());

        Optional<Specialty> fromDb = specialtyRepository.findById(UUID.fromString(String.valueOf(updated.id())));
        assertTrue(fromDb.isPresent());
        assertEquals(TEST_SPECIALTY_NAME_IN_UKRAINIAN, fromDb.get().getName().getUk());
    }

    @Nested
    @DisplayName("Get by id scenarios")
    @Transactional
    class GetByIdScenarios {
        @Test
        @DisplayName("Should successfully get Specialty by id when provided valid id")
        public void should_SuccessfullyGetSpecialtyById() {
            SpecialtyDto specialty = specialtyService.getById(testSpecialty.getId());

            assertNotNull(specialty);
            assertEquals(testSpecialty.getName().getEn(), specialty.name().getEn());
            assertEquals(testSpecialty.getSectors().size(), specialty.sectors().size());
            assertEquals(testSpecialty.getCategory(), specialty.category());
            assertEquals(testSpecialty.getCreatedAt(), specialty.createdAt());
            assertEquals(testSpecialty.getUpdatedAt(), specialty.updatedAt());
        }

        @Test
        @DisplayName("Should throw exception when provided not valid Specialty id")
        void should_ThrowExceptionWhenNotValidSpecialtyId() {
            assertThrows(SpecialtyException.class, () -> specialtyService.getById(UUID.randomUUID()));
        }
    }

//    @Nested
//    @DisplayName("Get all scenarios")
//    @Transactional
//    class GetAllScenarios {
//        @Test
//        @DisplayName("Should successfully get all Specialties by filter")
//        public void should_SuccessfullyGetAllSpecialtiesByFilter() {
//            for (int i = 0; i < 9; i++){
//                createTestSpecialty();
//            }
//
//            SpecialtyReceivingRequest specialtyReceivingRequest = new SpecialtyReceivingRequest(
//                    "name", multiLanguageFieldMapper.toDto(testSpecialty.getName()), SpecialtyCategory.SENIOR,
//                    LocalDateTime.of(2019, 1, 1, 0, 0),
//                    LocalDateTime.of(2023, 1, 1, 0, 0),
//                    LocalDateTime.of(2021, 1, 1, 0, 0),
//                    LocalDateTime.of(2024, 1, 1, 0, 0), 1,10
//            );
//
//            Page<SpecialtyDto> response = specialtyService.getAll(specialtyReceivingRequest);
//            SpecialtyDto firstResponseShortDto = response.get().findFirst().get();
//
//            assertNotNull(response);
//            assertEquals(10, response.getTotalElements());
//            assertEquals(testSpecialty.getName(), firstResponseShortDto.name());
//            assertEquals(testSpecialty.getCategory(), firstResponseShortDto.category());
//            assertEquals(testSpecialty.getCreatedAt(), firstResponseShortDto.createdAt());
//            assertEquals(testSpecialty.getUpdatedAt(), firstResponseShortDto.updatedAt());
//            assertEquals(testSpecialty.getSectors().size(), response.getTotalElements());
//        }
//    }

    @Test
    @DisplayName("Should successfully delete specialty when provided existing id")
    void should_SuccessfullyDeleteSector_When_ProvidedExistingId() {
        specialtyRepository.deleteById(testSector.getId());

        assertFalse(specialtyRepository.existsById(testSector.getId()));
    }

}
