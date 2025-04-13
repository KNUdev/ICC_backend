package ua.knu.knudev.fileserviceapi.api;

import org.springframework.web.multipart.MultipartFile;
import ua.knu.knudev.fileserviceapi.subfolder.FileSubfolder;

public interface BaseFileServiceApi<T extends FileSubfolder> {
    String uploadFile(MultipartFile file, String customFilename, T subfolder);

    String uploadFile(MultipartFile file, T subfolder);

    boolean existsByFilename(String filename, T subfolder);

    String getPathByFilename(String filename, T subfolder);

    void removeByFilename(String filename, T subfolder);

    String updateByFilename(String oldFilename, MultipartFile newFile, T subfolder);
}
