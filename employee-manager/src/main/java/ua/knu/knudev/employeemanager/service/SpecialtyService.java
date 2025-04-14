package ua.knu.knudev.employeemanager.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ua.knu.knudev.employeemanager.domain.Sector;
import ua.knu.knudev.employeemanager.domain.Specialty;
import ua.knu.knudev.employeemanager.mapper.MultiLanguageFieldMapper;
import ua.knu.knudev.employeemanager.mapper.SectorMapper;
import ua.knu.knudev.employeemanager.mapper.SpecialtyMapper;
import ua.knu.knudev.employeemanager.repository.SpecialtyRepository;
import ua.knu.knudev.employeemanagerapi.config.api.SpecialtyApi;
import ua.knu.knudev.employeemanagerapi.config.dto.SpecialtyDto;
import ua.knu.knudev.employeemanagerapi.config.exception.SpecialtyException;
import ua.knu.knudev.employeemanagerapi.config.request.SpecialtyCreationRequest;
import ua.knu.knudev.employeemanagerapi.config.request.SpecialtyUpdateRequest;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j

public class SpecialtyService implements SpecialtyApi {
    private final SpecialtyRepository specialtyRepository;
    private final SpecialtyMapper specialtyMapper;
    private final SectorMapper sectorMapper;
    private final MultiLanguageFieldMapper multiLanguageFieldMapper;

    @Override
    @Transactional
    public SpecialtyDto create(@Valid SpecialtyCreationRequest specialtyCreationRequest) {
        Set<Sector> sectors = new HashSet<>(sectorMapper.toDomains(specialtyCreationRequest.sectors()));

        Specialty specialty = Specialty.builder()
                .createdAt(LocalDateTime.now())
                .name(multiLanguageFieldMapper.toDomain(specialtyCreationRequest.name()))
                .category(specialtyCreationRequest.category())
                .sectors(sectors)
                .build();

        Specialty savedSpecialty = specialtyRepository.save(specialty);
        log.info("Created Specialty: {}", savedSpecialty);
        return specialtyMapper.toDto(savedSpecialty);
    };

    @Override
    @Transactional
    public SpecialtyDto getById(UUID specialtyId) {
        Specialty specialty = specialtyRepository.findById(specialtyId).orElseThrow(
                () -> new SpecialtyException("Specialty with id" + specialtyId + "not found"));
        return specialtyMapper.toDto(specialty);
    }

    @Transactional
    public Specialty getSpecialtyById(UUID id) {
        return specialtyRepository.findById(id)
                .orElseThrow(() -> new SpecialtyException("Specialty with id " + id + " not found"));
    }

    @Override
    public boolean existsById(UUID specialtyId) {
        return specialtyRepository.existsById(specialtyId);
    }

    @Override
    public List<SpecialtyDto> getAll() {
        List<Specialty> specialtyList = specialtyRepository.findAll();
        return specialtyMapper.toDtos(specialtyList);
    }

    @Override
    @Transactional
    public void delete(UUID specialtyId) {
        log.info("Deleting Specialty: {}", specialtyId);
        specialtyRepository.deleteById(specialtyId);
    }

    @Override
    @Transactional
    public SpecialtyDto update(@Valid SpecialtyUpdateRequest specialtyUpdateRequest, UUID specialtyId) {
        Specialty specialty = getSpecialtyById(specialtyId);

        log.info("Updating Specialty: {}", specialty);

        specialty.setUpdatedAt(LocalDateTime.now());
        specialty.setName(multiLanguageFieldMapper.toDomain(specialtyUpdateRequest.name()));
        specialty.setCategory(specialtyUpdateRequest.category());
        specialty.setSectors(sectorMapper.toDomains(specialtyUpdateRequest.sectors()));

        Specialty savedSpecialty = specialtyRepository.save(specialty);
        return specialtyMapper.toDto(savedSpecialty);
    }
}