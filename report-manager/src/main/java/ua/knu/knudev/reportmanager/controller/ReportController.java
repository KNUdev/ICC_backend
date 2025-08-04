package ua.knu.knudev.reportmanager.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.knu.knudev.reportmanager.generator.ReportGenerator;
import ua.knu.knudev.reportmanagerapi.dto.ReportRowDto;
import ua.knu.knudev.reportmanagerapi.service.ReportService;
import org.springframework.web.bind.annotation.RequestParam;
import ua.knu.knudev.reportmanagerapi.api.ReportServiceApi;

import java.util.List;
import java.io.File;

@RestController
@RequestMapping("/reports")
public class ReportController {
    private final ReportService service;
    private final @Qualifier("pdfReportGenerator") ReportGenerator pdfGen;
    private final @Qualifier("wordReportGenerator") ReportGenerator wordGen;
    private final ReportServiceApi reportService;

    public ReportController(
            ReportService service,
            @Qualifier("pdfReportGenerator") ReportGenerator pdfGen,
            @Qualifier("wordReportGenerator") ReportGenerator wordGen,
            ReportServiceApi reportService
    ) {
        this.service = service;
        this.pdfGen = pdfGen;
        this.wordGen = wordGen;
        this.reportService = reportService;
    }

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

    @GetMapping(path = "/pdf", produces = "application/pdf")
    public void pdf(HttpServletResponse resp) throws Exception {
        resp.setHeader("Content-Disposition", "attachment; filename=report.pdf");
        List<ReportRowDto> data = service.fetchReportData();
        pdfGen.generate(data, resp.getOutputStream());
    }

    @GetMapping(path = "/docx", produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
    public void docx(HttpServletResponse resp) throws Exception {
        resp.setHeader("Content-Disposition", "attachment; filename=report.docx");
        List<ReportRowDto> data = service.fetchReportData();
        wordGen.generate(data, resp.getOutputStream());
    }
}
