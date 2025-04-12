package ua.knu.knudev.fileserviceapi.subfolder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ImageSubfolder implements FileSubfolder {
    ACCOUNT_AVATARS("/account/avatars"),
    ACCOUNT_BANNERS("/account/banners"),
    TASK_PICTURES("/tasks/pictures"),
    PROJECTS_AVATARS("/projects/avatars");

    private final String subfolderPath;

    @Override
    public String getSubfolderPath() {
        return subfolderPath;
    }
}
