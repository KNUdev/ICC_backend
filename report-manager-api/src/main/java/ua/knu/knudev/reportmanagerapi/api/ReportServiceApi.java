package ua.knu.knudev.reportmanagerapi.api;

import java.io.File;

public interface ReportServiceApi {
    File createReportOfFormat(String formatType, String reportName);
}
