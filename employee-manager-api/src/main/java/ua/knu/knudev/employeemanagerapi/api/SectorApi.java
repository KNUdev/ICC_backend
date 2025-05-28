package ua.knu.knudev.employeemanagerapi.api;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import ua.knu.knudev.employeemanagerapi.dto.SectorDto;
import ua.knu.knudev.employeemanagerapi.request.SectorCreationRequest;
import ua.knu.knudev.employeemanagerapi.request.SectorReceivingRequest;
import ua.knu.knudev.employeemanagerapi.request.SectorUpdateRequest;

import java.util.UUID;

public interface SectorApi {
    SectorDto create(@Valid SectorCreationRequest sectorCreationRequest);

    SectorDto getById(UUID sectorId);

    Page<SectorDto> getAll(SectorReceivingRequest request);

    void delete(UUID sectorId);

    SectorDto update(@Valid SectorUpdateRequest sectorUpdateRequest);
}
