package ua.knu.knudev.fileserviceapi.api;

import ua.knu.knudev.fileserviceapi.subfolder.PdfSubfolderI;

public interface PDFServiceApi extends BaseFileServiceApi<PdfSubfolderI> {
    String getFile();
}
