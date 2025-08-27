package ua.knu.knudev.employeemanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ua.knu.knudev.employeemanagerapi.devprofile.DevProfileEmployeeManagerApi;
import ua.knu.knudev.employeemanagerapi.dto.SectorDto;
import ua.knu.knudev.employeemanagerapi.dto.SpecialtyDto;
import ua.knu.knudev.employeemanagerapi.request.SectorCreationRequest;
import ua.knu.knudev.employeemanagerapi.request.SpecialtyCreationRequest;
import ua.knu.knudev.icccommon.constant.SpecialtyCategory;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;

import java.util.*;
import java.util.stream.Collectors;

@Profile("dev")
@Service
@RequiredArgsConstructor
public class DevProfileEmployeeManager implements DevProfileEmployeeManagerApi {

    private static final String ENGLISH_LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String UKRAINIAN_LETTERS = "абвгґдеєжзийіїклмнопрстуфхцчшщьюяАБВГҐДЕЄЖЗИЙІЇКЛМНОПРСТУФХЦЧШЩЬЮЯ";
    private static final Random RANDOM = new Random();
    private final SectorService sectorService;
    private final SpecialtyService specialtyService;

    @Override
    public List<SectorDto> createTestSectors(int numberOfSectors) {
        List<SectorDto> sectors = new ArrayList<>();

        for (int i = 0; i < numberOfSectors; i++) {
            SectorDto newSector = createSector();
            sectors.add(newSector);
        }

        return sectors;
    }

    @Override
    public List<SpecialtyDto> createTestSpecialties(int numberOfSpecialties) {
        List<SpecialtyDto> specialties = new ArrayList<>();
        Set<SectorDto> sectors = new HashSet<>();
        sectors.add(createSector());

        for (int i = 0; i < numberOfSpecialties; i++) {
            MultiLanguageFieldDto name =
                    new MultiLanguageFieldDto(
                            "specialty " + getRandomString(ENGLISH_LETTERS),
                            "спеціальність " + getRandomString(UKRAINIAN_LETTERS)
                    );
            SpecialtyCreationRequest request = SpecialtyCreationRequest.builder()
                    .name(name)
                    .sectorsIds(sectors.stream()
                            .map(SectorDto::id)
                            .collect(Collectors.toSet()))
                    .category(SpecialtyCategory.FIRST)
                    .build();

            SpecialtyDto newSpecialty = specialtyService.create(request);
            specialties.add(newSpecialty);
        }

        return specialties;
    }

    private SectorDto createSector() {
        MultiLanguageFieldDto name =
                new MultiLanguageFieldDto("sector " + getRandomString(ENGLISH_LETTERS), "сектор " + getRandomString(UKRAINIAN_LETTERS));
        SectorCreationRequest request = SectorCreationRequest.builder()
                .name(name)
                .specialtiesIds(new HashSet<>())
                .build();

        return sectorService.create(request);
    }

    private String getRandomString(String letters) {
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(letters.charAt(RANDOM.nextInt(letters.length())));
        }
        return sb.toString();
    }

}
