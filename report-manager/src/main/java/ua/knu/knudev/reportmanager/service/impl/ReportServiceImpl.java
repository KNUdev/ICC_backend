package ua.knu.knudev.reportmanager.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.knu.knudev.reportmanager.domain.ReportRowEntity;
import ua.knu.knudev.reportmanager.repository.ReportRowRepository;
import ua.knu.knudev.reportmanagerapi.dto.ReportRowDto;
import ua.knu.knudev.reportmanagerapi.service.ReportService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final ReportRowRepository repo;

    public ReportServiceImpl(ReportRowRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<ReportRowDto> fetchReportData() {
        var rows = repo.findAll();
        log.info(">>> fetching {} rows …", rows.size());
        return rows.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private ReportRowDto toDto(ReportRowEntity e) {
        return new ReportRowDto(
                e.getId(),
                e.getFullName(),
                e.getPhoneNumber(),
                e.getEmail(),
                e.getPosition(),
                e.getSalary(),
                e.getContractValidTo()
        );
    }
}
