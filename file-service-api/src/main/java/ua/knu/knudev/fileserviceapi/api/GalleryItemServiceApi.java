package ua.knu.knudev.fileserviceapi.api;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import ua.knu.knudev.fileserviceapi.dto.GalleryItemDto;
import ua.knu.knudev.fileserviceapi.request.GalleryItemReceivingRequest;
import ua.knu.knudev.fileserviceapi.request.GalleryItemUpdateRequest;
import ua.knu.knudev.fileserviceapi.request.GalleryItemUploadRequest;

import java.util.UUID;

public interface GalleryItemServiceApi {
    public GalleryItemDto upload(@Valid GalleryItemUploadRequest request, UUID employeeId);

    public Page<GalleryItemDto> getAll(GalleryItemReceivingRequest request);

    public void delete(UUID creatorID);

    public GalleryItemDto update(@Valid GalleryItemUpdateRequest request);

    public GalleryItemDto getDtoById(UUID id);
}
