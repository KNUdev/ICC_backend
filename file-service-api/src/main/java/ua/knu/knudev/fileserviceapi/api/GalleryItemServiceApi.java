package ua.knu.knudev.fileserviceapi.api;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import ua.knu.knudev.fileserviceapi.dto.GalleryItemDto;
import ua.knu.knudev.fileserviceapi.request.GalleryItemUpdateRequest;
import ua.knu.knudev.fileserviceapi.request.GalleryItemUploadRequest;

import java.util.UUID;

public interface GalleryItemServiceApi {
    GalleryItemDto upload(@Valid GalleryItemUploadRequest request);

    Page<GalleryItemDto> getAll(int page, int size);

    void delete(UUID creatorID);

    GalleryItemDto update(@Valid GalleryItemUpdateRequest request);

    GalleryItemDto getById(UUID id);
}
