package ua.knu.knudev.employeemanagerapi.api;

import org.springframework.data.domain.Page;
import ua.knu.knudev.employeemanagerapi.dto.SpecialtyDto;
import ua.knu.knudev.employeemanagerapi.request.SpecialtyCreationRequest;
import ua.knu.knudev.employeemanagerapi.request.SpecialtyUpdateRequest;

import java.util.UUID;

public interface SpecialtyApi {
    SpecialtyDto create(SpecialtyCreationRequest specialtyCreationRequest);

    SpecialtyDto getById(UUID specialtyId);

    boolean existsById(UUID specialtyId);

    void delete(UUID specialtyId);

    SpecialtyDto update(SpecialtyUpdateRequest specialtyUpdateRequest);

    Page<SpecialtyDto> getAll();
}
