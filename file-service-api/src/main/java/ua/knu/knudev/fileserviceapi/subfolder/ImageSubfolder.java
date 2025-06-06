package ua.knu.knudev.fileserviceapi.subfolder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ImageSubfolder implements FileSubfolder {
    EMPLOYEE_AVATARS("/employee/avatars"),
    GALLERY("/gallery");

    private final String subfolderPath;

    @Override
    public String getSubfolderPath() {
        return subfolderPath;
    }
}
