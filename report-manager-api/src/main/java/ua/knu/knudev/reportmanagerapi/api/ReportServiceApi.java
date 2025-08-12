package ua.knu.knudev.reportmanagerapi.api;

import ua.knu.knudev.icccommon.enums.ReportFormat;

import java.io.File;

public interface ReportServiceApi {
    File createReportOfFormat(ReportFormat formatType, String reportName);
}
