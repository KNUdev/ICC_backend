package ua.knu.knudev.fileserviceapi.dto;

import lombok.Builder;

import java.io.InputStream;

@Builder
public record FileUploadPayload(InputStream inputStream, String fileName, FolderPath folderPath) {
}
