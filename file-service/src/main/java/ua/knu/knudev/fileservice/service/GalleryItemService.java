package ua.knu.knudev.fileservice.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import ua.knu.knudev.employeemanagerapi.api.EmployeeApi;
import ua.knu.knudev.employeemanagerapi.exception.EmployeeException;
import ua.knu.knudev.fileservice.domain.GalleryItem;
import ua.knu.knudev.fileservice.mapper.GalleryItemMapper;
import ua.knu.knudev.fileservice.repository.GalleryItemRepository;
import ua.knu.knudev.fileserviceapi.api.GalleryItemServiceApi;
import ua.knu.knudev.fileserviceapi.api.ImageServiceApi;
import ua.knu.knudev.fileserviceapi.dto.GalleryItemDto;
import ua.knu.knudev.fileserviceapi.exception.GalleryItemException;
import ua.knu.knudev.fileserviceapi.request.GalleryItemUpdateRequest;
import ua.knu.knudev.fileserviceapi.request.GalleryItemUploadRequest;
import ua.knu.knudev.fileserviceapi.subfolder.ImageSubfolder;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class GalleryItemService implements GalleryItemServiceApi {

    private final GalleryItemMapper galleryItemMapper;
    private final GalleryItemRepository galleryItemRepository;
    private final EmployeeApi employeeApi;
    private final ImageServiceApi imageServiceApi;

    @Override
    @Transactional
    public GalleryItemDto upload(@Valid GalleryItemUploadRequest request) {
        if (!employeeApi.existsById(request.creatorId())) {
            throw new EmployeeException("Employee does not exist");
        }

        MultipartFile file = request.item();
        String savedGalleryItemName = uploadImage(file, request.itemName(), ImageSubfolder.GALLERY);

        GalleryItem image = GalleryItem.builder()
                .uploadedAt(LocalDateTime.now())
                .creatorId(request.creatorId())
                .itemName(savedGalleryItemName)
                .itemDescription(request.itemDescription())
                .build();

        GalleryItem savedImage = galleryItemRepository.save(image);
        log.info("Saved image {}", savedImage);
        return galleryItemMapper.toDto(savedImage);
    }

    @Override
    @Transactional
    public Page<GalleryItemDto> getAll(int pageNumber, int pageSize) {
        Pageable paging = PageRequest.of(1, 10, Sort.by("uploadedAt").descending());
        Page<GalleryItem> galleryItems = galleryItemRepository.findAll(paging);

        return galleryItems.map(galleryItemMapper::toDto);
    }

    @Override
    public void delete(UUID itemId) {
        galleryItemRepository.deleteById(itemId);
    }

    @Override
    @Transactional
    public GalleryItemDto update(@Valid GalleryItemUpdateRequest request) {
        GalleryItem item = getGalleryItemById(request.itemId());

        item.setUpdatedAt(LocalDateTime.now());

        item.setItemDescription(getOrDefault(request.itemDescription(), item.getItemDescription()));

        if (request.item() != null) {
            String newImage = uploadImage(request.item(), request.itemName(), ImageSubfolder.GALLERY);
            item.setItemName(newImage);
        }
        GalleryItem savedItem = galleryItemRepository.save(item);
        return galleryItemMapper.toDto(savedItem);
    }

    @Override
    @Transactional
    public GalleryItemDto getById(UUID id) {
        GalleryItem item = galleryItemRepository.findById(id).orElseThrow(
                () -> new GalleryItemException("GalleryItem with id " + id + " not found")
        );
        return galleryItemMapper.toDto(item);
    }

    public GalleryItem getGalleryItemById(UUID id) {
        return galleryItemRepository.findById(id).orElseThrow(
                () -> new GalleryItemException("GalleryItem with id " + id + " not found"));
    }

    private String uploadImage(MultipartFile imageFile, String imageName, ImageSubfolder subfolder) {
        if (ObjectUtils.isEmpty(imageFile)) {
            return null;
        }
        return imageServiceApi.uploadFile(imageFile, imageName, subfolder);
    }

    private <T> T getOrDefault(T newValue, T currentValue) {
        return newValue != null ? newValue : currentValue;
    }
}