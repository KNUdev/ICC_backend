package ua.knu.knudev.employeemanager.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
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
import ua.knu.knudev.employeemanagerapi.api.SpecialtyApi;
import ua.knu.knudev.employeemanagerapi.dto.SectorDto;
import ua.knu.knudev.employeemanagerapi.dto.SpecialtyDto;
import ua.knu.knudev.employeemanagerapi.exception.SpecialtyException;
import ua.knu.knudev.employeemanagerapi.request.SpecialtyCreationRequest;
import ua.knu.knudev.employeemanagerapi.request.SpecialtyReceivingRequest;
import ua.knu.knudev.employeemanagerapi.request.SpecialtyUpdateRequest;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;
import ua.knu.knudev.icccommon.mapper.MultiLanguageFieldMapper;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class SpecialtyService implements SpecialtyApi {

    private final SpecialtyRepository specialtyRepository;
    private final SpecialtyMapper specialtyMapper;
    private final SectorMapper sectorMapper;
    private final MultiLanguageFieldMapper multiLanguageFieldMapper;
    private final SectorRepository sectorRepository;

    @Override
    @Transactional
    public SpecialtyDto create(@Valid SpecialtyCreationRequest specialtyCreationRequest) {
        Set<UUID> ids = specialtyCreationRequest.sectors().stream()
                .map(SectorDto::id)
                .collect(Collectors.toSet());

        Set<Sector> existingSectors = new HashSet<>(sectorRepository.findAllById(ids));

        Specialty specialty = Specialty.builder()
                .createdAt(LocalDateTime.now())
                .name(multiLanguageFieldMapper.toDomain
                        (specialtyCreationRequest.name()))
                .category(specialtyCreationRequest.category())
                .sectors(new HashSet<>())
                .build();

        specialty.addSectors(existingSectors);

        Specialty savedSpecialty = specialtyRepository.save(specialty);
        log.info("Created Specialty: {}", savedSpecialty);
        return specialtyMapper.toDto(savedSpecialty);
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
        checkIsNameValid(specialtyUpdateRequest.name());
        Specialty specialty = getSpecialtyById(specialtyUpdateRequest.id());

        specialty.setUpdatedAt(LocalDateTime.now());

        specialty.setName(getOrDefault(specialtyUpdateRequest.name(), specialty.getName(),
                multiLanguageFieldMapper::toDomain));

        specialty.setCategory(getOrDefault(specialtyUpdateRequest.category(),
                specialty.getCategory()));

        if (specialtyUpdateRequest.sectors() != null) {
            Set<Sector> sectors = sectorMapper.toDomains(specialtyUpdateRequest.sectors());
            specialty.removeAllSectors(sectors);
            specialty.addSectors(sectors);
        }

        Specialty savedSpecialty = specialtyRepository.save(specialty);
        return specialtyMapper.toDto(savedSpecialty);
    }

    @Transactional
    public SpecialtyDto getById(UUID specialtyId) {
        Specialty specialty = specialtyRepository.findById(specialtyId).orElseThrow(()
                -> new SpecialtyException("Specialty with id" + specialtyId + "not found"));
        return specialtyMapper.toDto(specialty);
    }

    public Specialty getSpecialtyById(UUID id) {
        return specialtyRepository.findById(id).orElseThrow(
                () -> new SpecialtyException("Specialty with id " + id + " not found"));
    }

    private void checkIsNameValid(MultiLanguageFieldDto name) {
        if (ObjectUtils.isEmpty(name)) {
            return;
        }

        if (name.getEn() != null && !Pattern.matches("^[A-Za-z\\s-]+$", name.getEn())) {
            throw new SpecialtyException("English name must contain only English letters, hyphens and spaces. Instead got:" +
                    " '" + name.getEn() + "'");
        }
        if (name.getUk() != null && !Pattern.matches("^[А-Яа-яЇїІіЄєҐґ\\s'’-]+$", name.getUk())) {
            throw new SpecialtyException("Ukrainian name must contain only Ukrainian letters, hyphens, apostrophes and spaces." +
                    " Instead got: '" + name.getUk() + "'");
        }
    }

    private <T> T getOrDefault(T newValue, T currentValue) {
        return newValue != null ? newValue : currentValue;
    }

    private <T, R> R getOrDefault(T newValue, R currentValue, Function<T, R> mapper) {
        return newValue != null ? Objects.requireNonNullElse(mapper.apply(newValue), currentValue) : currentValue;
    }
}