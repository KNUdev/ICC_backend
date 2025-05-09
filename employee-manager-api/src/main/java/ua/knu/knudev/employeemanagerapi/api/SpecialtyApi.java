package ua.knu.knudev.employeemanagerapi.api;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import ua.knu.knudev.employeemanagerapi.dto.SpecialtyDto;
import ua.knu.knudev.employeemanagerapi.request.SpecialtyCreationRequest;
import ua.knu.knudev.employeemanagerapi.request.SpecialtyReceivingRequest;
import ua.knu.knudev.employeemanagerapi.request.SpecialtyUpdateRequest;

import java.util.UUID;

public interface SpecialtyApi {
    SpecialtyDto create(@Valid SpecialtyCreationRequest specialtyCreationRequest);

    SpecialtyDto getById(UUID specialtyId);

    boolean existsById(UUID specialtyId);

    void delete(UUID specialtyId);

    SpecialtyDto update(@Valid SpecialtyUpdateRequest specialtyUpdateRequest);

    Page<SpecialtyDto> getAll(SpecialtyReceivingRequest specialtyReceivingRequest);
}
