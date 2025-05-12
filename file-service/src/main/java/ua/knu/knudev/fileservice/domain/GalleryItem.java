package ua.knu.knudev.fileservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "gallery_manager", name = "gallery_item")
@Builder
public class GalleryItem {
    @Id
    @UuidGenerator
    @Column(nullable = false, updatable = false)
    private UUID creatorId;

    @Column(nullable = false)
    private String itemName;

    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadedAt;

    private LocalDateTime updatedAt;
}