package ua.knu.knudev.employeemanagerapi.config.api;

import ua.knu.knudev.employeemanagerapi.config.dto.SpecialtyDto;
import ua.knu.knudev.employeemanagerapi.config.request.SpecialtyCreationRequest;
import ua.knu.knudev.employeemanagerapi.config.request.SpecialtyUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface SpecialtyApi {
    public SpecialtyDto create(SpecialtyCreationRequest specialtyCreationRequest);

    public SpecialtyDto getById(UUID specialtyId);

    public boolean existsById(UUID specialtyId);

    public void delete(UUID specialtyId);

    public SpecialtyDto update(SpecialtyUpdateRequest specialtyUpdateRequest, UUID specialtyId);

    public List<SpecialtyDto> getAll();
}
