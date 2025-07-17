package ua.knu.knudev.reportmanager.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.knu.knudev.reportmanagerapi.api.reportServiceApi;

import java.io.File;

@RequestMapping("/report")
public class ReportController {

    private final reportServiceApi reportService;

    public ReportController(reportServiceApi reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/create")
    public ResponseEntity<Resource> create(@RequestParam(value = "FormatType", required = true) String formatType,
                                           @RequestParam(value = "ReportName", required = true) String reportName) {
        File file = reportService.createReportOfFormat(formatType, reportName);
        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                .contentLength(file.length())
                .body(resource);
    }
}
