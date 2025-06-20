package ua.knu.knudev.employeemanagerapi.api;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public sealed interface AvatarManagementApi permits EmployeeApi {

    String addAvatar(MultipartFile file);

    String updateAvatar(UUID id, MultipartFile avatarFile);

    void removeAvatar(UUID employeeId);
    
}
