package ua.knu.knudev.fileserviceapi.dto;

import lombok.Builder;

@Builder
public record FolderPath(String path, String subfolderPath) {
}
