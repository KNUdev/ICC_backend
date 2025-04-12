package ua.knu.knudev.fileserviceapi.folder;

import ua.knu.knudev.fileserviceapi.subfolder.FileSubfolder;

import java.util.List;

public interface FileFolder<S extends FileSubfolder> {
    String getName();
    List<S> getSubfolders();
}
