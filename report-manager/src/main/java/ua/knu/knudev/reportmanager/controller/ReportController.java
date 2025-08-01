package ua.knu.knudev.reportmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.knu.knudev.reportmanagerapi.api.ReportServiceApi;

import java.io.File;

@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {
    private final ReportServiceApi reportService;

    @PostMapping("/create")
    public ResponseEntity<Resource> create(@RequestParam(value = "formatType", required = true) String formatType,
                                           @RequestParam(value = "reportName", required = true) String reportName) {
        File file = reportService.createReportOfFormat(formatType, reportName);
        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                .contentLength(file.length())
                .body(resource);
    }
}
