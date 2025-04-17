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
import ua.knu.knudev.employeemanager.mapper.MultiLanguageFieldMapper;
import ua.knu.knudev.employeemanager.mapper.SectorMapper;
import ua.knu.knudev.employeemanager.mapper.SpecialtyMapper;
import ua.knu.knudev.employeemanager.repository.SectorRepository;
import ua.knu.knudev.employeemanagerapi.api.SectorApi;
import ua.knu.knudev.employeemanagerapi.dto.SectorDto;
import ua.knu.knudev.employeemanagerapi.exception.SectorException;
import ua.knu.knudev.employeemanagerapi.request.SectorCreationRequest;
import ua.knu.knudev.employeemanagerapi.request.SectorReceivingRequest;
import ua.knu.knudev.employeemanagerapi.request.SectorUpdateRequest;


import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class SectorService implements SectorApi {
    private final SectorRepository sectorRepository;
    private final SpecialtyMapper specialtyMapper;
    private final SectorMapper sectorMapper;
    private final MultiLanguageFieldMapper multiLanguageFieldMapper;

    @Override
    public SectorDto create(@Valid SectorCreationRequest request) {
        Set<Specialty> specialties = specialtyMapper.toDomains(
                request.specialties()
        );

        Sector sector = Sector.builder()
                .createdAt(LocalDateTime.now())
                .name(multiLanguageFieldMapper.toDomain(request.name()))
                .specialties(specialties)
                .build();

        Sector savedSector = sectorRepository.save(sector);
        log.info("Created Sector: {}", savedSector);
        return sectorMapper.toDto(savedSector);
    }

    @Override
    public SectorDto getById(UUID sectorId) {
        Sector sector = getSectorById(sectorId);
        return sectorMapper.toDto(sector);
    }

    @Override
    public Page<SectorDto> getAll(SectorReceivingRequest request) {
        Pageable paging = PageRequest.of(request.pageNumber(), request.pageSize());
        Page<Sector> sectorsPage = sectorRepository.findAllBySearchQuery(
                paging, request.searchQuery(), request.createdAt(), request.updatedAt()
        );

        return sectorsPage.map(sectorMapper::toDto);
    }

    @Override
    public boolean existsById(UUID sectorId) {
        return sectorRepository.existsById(sectorId);
    }

    @Override
    @Transactional
    public SectorDto update(@Valid SectorUpdateRequest request) {
        Sector sector = getSectorById(request.id());

        sector.setUpdatedAt(LocalDateTime.now());
        sector.setName(getOrDefault(
                request.name(),
                sector.getName(),
                multiLanguageFieldMapper::toDomain
        ));
        sector.setSpecialties(getOrDefault(
                request.specialties(),
                sector.getSpecialties(),
                specialtyMapper::toDomains
                ));

        Sector savedSector = sectorRepository.save(sector);
        return sectorMapper.toDto(savedSector);
    }

    @Override
    public void delete(UUID sectorId) {
        sectorRepository.deleteById(sectorId);
    }


    private Sector getSectorById(UUID id) {
        return sectorRepository.findById(id).orElseThrow(
                () -> new SectorException("Sector with id " + id + " not found"));
    }

    private <T, R> R getOrDefault(T newValue, R currentValue, Function<T, R> mapper) {
        return newValue != null ? Objects.requireNonNullElse(mapper.apply(newValue), currentValue) : currentValue;
    }
}
