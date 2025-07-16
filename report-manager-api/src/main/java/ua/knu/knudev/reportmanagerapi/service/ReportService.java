package ua.knu.knudev.reportmanagerapi.service;

import ua.knu.knudev.reportmanagerapi.dto.ReportRowDto;
import java.util.List;

public interface ReportService {
    List<ReportRowDto> fetchReportData();
}
