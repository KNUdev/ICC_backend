package ua.knu.knudev.employeemanagerapi.api;

import org.springframework.data.domain.Page;
import ua.knu.knudev.employeemanagerapi.dto.SectorDto;
import ua.knu.knudev.employeemanagerapi.request.SectorCreationRequest;
import ua.knu.knudev.employeemanagerapi.request.SectorReceivingRequest;
import ua.knu.knudev.employeemanagerapi.request.SectorUpdateRequest;

import java.util.UUID;

public interface SectorApi {
    SectorDto create(SectorCreationRequest sectorCreationRequest);

    SectorDto getById(UUID sectorId);

    Page<SectorDto> getAll(SectorReceivingRequest request);

    void delete(UUID sectorId);

    boolean existsById(UUID sectorId);

    SectorDto update(SectorUpdateRequest sectorUpdateRequest);
}
