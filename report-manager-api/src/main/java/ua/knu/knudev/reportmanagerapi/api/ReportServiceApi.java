package ua.knu.knudev.reportmanagerapi.api;

import java.io.File;

public interface ReportServiceApi {

    public File extractReportToExcel(String reportName);
    public File extractReportToCSV(String reportName);
    public File createReportOfFormat(String formatType, String reportName);
}
