package ua.knu.knudev.fileserviceapi.folder;

import ua.knu.knudev.fileserviceapi.subfolder.PdfSubfolder;
import ua.knu.knudev.fileserviceapi.subfolder.PdfSubfolderI;

import java.util.List;

public enum PdfFolder implements FileFolder<PdfSubfolderI> {
    INSTANCE;

    @Override
    public String getName() {
        return "pdfs";
    }

    @Override
    public List<PdfSubfolderI> getSubfolders() {
        return List.of(PdfSubfolder.values());
    }
}
