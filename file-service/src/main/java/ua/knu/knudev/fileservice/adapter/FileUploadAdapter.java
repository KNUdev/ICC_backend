package ua.knu.knudev.fileservice.adapter;

import ua.knu.knudev.fileserviceapi.dto.FileUploadPayload;
import ua.knu.knudev.fileserviceapi.folder.FileFolderProperties;
import ua.knu.knudev.fileserviceapi.subfolder.FileSubfolder;

public interface FileUploadAdapter {
    String saveFile(FileUploadPayload payload);

    boolean existsByFilename(String filename, FileFolderProperties<? extends FileSubfolder> fileFolderProperties);

    String getPathByFilename(String filename, FileFolderProperties<? extends FileSubfolder> fileFolderProperties);

    void deleteByFilename(String filename, FileFolderProperties<? extends FileSubfolder> fileFolderProperties);

}
