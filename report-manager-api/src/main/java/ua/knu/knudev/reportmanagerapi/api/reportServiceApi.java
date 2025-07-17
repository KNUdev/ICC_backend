package ua.knu.knudev.reportmanagerapi.api;

import java.io.File;

public interface reportServiceApi {

    File extractReportToExcel(String reportName);
    File extractReportToCSV(String reportName);
    File createReportOfFormat(String formatType, String reportName);
}
