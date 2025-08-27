package ua.knu.knudev.employeemanager.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ua.knu.knudev.employeemanager.domain.Sector;
import ua.knu.knudev.employeemanager.domain.Specialty;
import ua.knu.knudev.employeemanager.mapper.SectorMapper;
import ua.knu.knudev.employeemanager.mapper.SpecialtyMapper;
import ua.knu.knudev.employeemanager.repository.SectorRepository;
import ua.knu.knudev.employeemanager.repository.SpecialtyRepository;
import ua.knu.knudev.employeemanagerapi.api.SectorApi;
import ua.knu.knudev.employeemanagerapi.dto.SectorDto;
import ua.knu.knudev.employeemanagerapi.exception.SectorException;
import ua.knu.knudev.employeemanagerapi.request.SectorCreationRequest;
import ua.knu.knudev.employeemanagerapi.request.SectorReceivingRequest;
import ua.knu.knudev.employeemanagerapi.request.SectorUpdateRequest;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;
import ua.knu.knudev.icccommon.mapper.MultiLanguageFieldMapper;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class SectorService implements SectorApi {
    private final SectorRepository sectorRepository;
    private final SectorMapper sectorMapper;
    private final MultiLanguageFieldMapper multiLanguageFieldMapper;
    private final SpecialtyRepository specialtyRepository;

    @Override
    @Transactional
    public SectorDto create(@Valid SectorCreationRequest request) {
        Set<Specialty> existingSpecialties = new HashSet<>();

        if (!request.specialtiesIds().isEmpty()) {
            Set<UUID> ids = request.specialtiesIds();
            existingSpecialties = new HashSet<>(specialtyRepository.findAllById(ids));


        }

        Sector sector = Sector.builder()
                .createdAt(LocalDateTime.now())
                .name(multiLanguageFieldMapper.toDomain(request.name()))
                .specialties(new HashSet<>())
                .build();

        sector.addSpecialties(existingSpecialties);
        Sector savedSector = sectorRepository.save(sector);
        log.info("Created Sector: {}", savedSector);
        return sectorMapper.toDto(savedSector);
    }

    @Override
    @Transactional
    public SectorDto getById(UUID sectorId) {
        Sector sector = getSectorById(sectorId);
        return sectorMapper.toDto(sector);
    }

    @Override
    @Transactional
    public Page<SectorDto> getAll(SectorReceivingRequest request) {
        int pageNumber = getOrDefault(request.pageNumber(), 0);
        int pageSize = getOrDefault(request.pageSize(), 10);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Sector> sectorsPage = sectorRepository.findAllBySearchQuery(pageable, request);

        return sectorsPage.map(sectorMapper::toDto);
    }

    @Override
    @Transactional
    public SectorDto update(@Valid SectorUpdateRequest request) {
        checkIsNameValid(request.name());

        Sector sector = getSectorById(request.id());
        sector.setUpdatedAt(LocalDateTime.now());
        sector.setName(getOrDefault(
                request.name(),
                sector.getName(),
                multiLanguageFieldMapper::toDomain
        ));
        if (request.specialtiesIds() != null) {
            Set<Specialty> specialties = new HashSet<>(specialtyRepository.findAllById(request.specialtiesIds()));
            validateSpecialtiesNonExistence(specialties, request.specialtiesIds());

            sector.removeAllSpecialties();
            sector.addSpecialties(specialties);
        }

        Sector savedSector = sectorRepository.save(sector);
        return sectorMapper.toDto(savedSector);
    }

    @Override
    public void delete(UUID sectorId) {
        sectorRepository.deleteById(sectorId);
    }

    public boolean existsById(UUID sectorId) {
        return sectorRepository.existsById(sectorId);
    }

    private void checkIsNameValid(MultiLanguageFieldDto name) {
        if (name == null) {
            return;
        }

        if (name.getEn() != null && !Pattern.matches("^[A-Za-z\\s-]+$", name.getEn())) {
            throw new SectorException("English name must contain only English letters, hyphens and spaces. Instead got:" +
                    " '" + name.getEn() + "'");
        }
        if (name.getUk() != null && !Pattern.matches("^[А-Яа-яЇїІіЄєҐґ\\s'’-]+$", name.getUk())) {
            throw new SectorException("Ukrainian name must contain only Ukrainian letters, hyphens, apostrophes and spaces." +
                    " Instead got: '" + name.getUk() + "'");
        }
    }

    private void validateSpecialtiesNonExistence(Set<Specialty> existentSpecialties, Set<UUID> presumedSpecialtiesIds) {
        if (existentSpecialties.size() != presumedSpecialtiesIds.size()) {
            throw new SectorException("Can not update sector. Attached specialty id not found");
        }
    }

    private Sector getSectorById(UUID id) {
        return sectorRepository.findById(id).orElseThrow(
                () -> new SectorException("Sector with id " + id + " not found"));
    }

    private <T, R> R getOrDefault(T newValue, R currentValue, Function<T, R> mapper) {
        return newValue != null ? Objects.requireNonNullElse(mapper.apply(newValue), currentValue) : currentValue;
    }

    private <T> T getOrDefault(T newValue, T currentValue) {
        return newValue != null ? newValue : currentValue;
    }
}
