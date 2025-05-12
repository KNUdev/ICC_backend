package ua.knu.knudev.fileservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.knu.knudev.fileservice.domain.GalleryItem;

import java.util.UUID;

public interface GalleryItemRepository extends JpaRepository<GalleryItem, UUID> {
}