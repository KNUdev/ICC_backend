package ua.knu.knudev.reportmanager.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.knu.knudev.reportmanagerapi.dto.ReportRowDto;
import ua.knu.knudev.reportmanagerapi.service.ReportService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    @Override
    public List<ReportRowDto> fetchReportData() {
        return List.of(
                new ReportRowDto(1L, "Іван Іванович", LocalDate.now().minusDays(1), BigDecimal.valueOf(123.45)),
                new ReportRowDto(2L, "Петро Петренко", LocalDate.now(), BigDecimal.valueOf(543.21))
        );
    }
}
