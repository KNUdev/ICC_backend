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
import ua.knu.knudev.employeemanager.repository.SpecialtyRepository;
import ua.knu.knudev.employeemanagerapi.api.SpecialtyApi;
import ua.knu.knudev.employeemanagerapi.dto.SpecialtyDto;
import ua.knu.knudev.employeemanagerapi.exception.SpecialtyException;
import ua.knu.knudev.employeemanagerapi.request.SpecialtyCreationRequest;
import ua.knu.knudev.employeemanagerapi.request.SpecialtyReceivingRequest;
import ua.knu.knudev.employeemanagerapi.request.SpecialtyUpdateRequest;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

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
        Set<Sector> sectors = sectorMapper.toDomains
                (specialtyCreationRequest.sectors());

        Specialty specialty = Specialty.builder()
                .createdAt(LocalDateTime.now())
                .name(multiLanguageFieldMapper.toDomain
                        (specialtyCreationRequest.name()))
                .category(specialtyCreationRequest.category())
                .sectors(sectors)
                .build();

        Specialty savedSpecialty = specialtyRepository.save(specialty);
        log.info("Created Specialty: {}", savedSpecialty);
        return specialtyMapper.toDto(savedSpecialty);
    }

    @Transactional
    public SpecialtyDto getById(UUID specialtyId) {
        Specialty specialty = specialtyRepository.findById(specialtyId).orElseThrow(()
                -> new SpecialtyException("Specialty with id" + specialtyId + "not found"));
        return specialtyMapper.toDto(specialty);
    }

    public boolean existsById(UUID specialtyId) {
        return specialtyRepository.existsById(specialtyId);
    }

    @Override
    @Transactional
    public Page<SpecialtyDto> getAll(SpecialtyReceivingRequest receivingRequest) {
        Pageable paging = PageRequest.of(receivingRequest.pageNumber(), receivingRequest.pageSize());
        Page<Specialty> specialtyPage = specialtyRepository.getSpecialtiesByFilter(paging, receivingRequest);

        return specialtyPage.map(specialtyMapper::toDto);
    }

    @Override
    public void delete(UUID specialtyId) {
        specialtyRepository.deleteById(specialtyId);
    }

    @Override
    @Transactional
    public SpecialtyDto update(@Valid SpecialtyUpdateRequest specialtyUpdateRequest) {
        Specialty specialty = getSpecialtyById(specialtyUpdateRequest.id());

        specialty.setUpdatedAt(LocalDateTime.now());

        specialty.setName(getOrDefault(specialtyUpdateRequest.name(), specialty.getName(),
                multiLanguageFieldMapper::toDomain));

        specialty.setCategory(getOrDefault(specialtyUpdateRequest.category(),
                specialty.getCategory()));

        specialty.setSectors(getOrDefault(specialtyUpdateRequest.sectors(), specialty.getSectors(),
                sectorMapper::toDomains));

        Specialty savedSpecialty = specialtyRepository.save(specialty);
        return specialtyMapper.toDto(savedSpecialty);
    }

    public Specialty getSpecialtyById(UUID id) {
        return specialtyRepository.findById(id).orElseThrow(
                () -> new SpecialtyException("Specialty with id " + id + " not found"));
    }

    private <T> T getOrDefault(T newValue, T currentValue) {
        return newValue != null ? newValue : currentValue;
    }

    private <T, R> R getOrDefault(T newValue, R currentValue, Function<T, R> mapper) {
        return newValue != null ? Objects.requireNonNullElse(mapper.apply(newValue), currentValue) : currentValue;
    }

    private <T, R> R mapIfNull(R currentValue, T newValue, Function<T, R> mapper) {
        return (currentValue == null && newValue != null) ? mapper.apply(newValue) : currentValue;
    }
}