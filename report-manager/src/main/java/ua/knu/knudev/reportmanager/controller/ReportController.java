package ua.knu.knudev.reportmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.knu.knudev.reportmanagerapi.dto.ReportRowDto;
import ua.knu.knudev.reportmanagerapi.service.ReportService;
import ua.knu.knudev.reportmanager.generator.ReportGenerator;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService service;
    private final @Qualifier("pdfReportGenerator") ReportGenerator pdfGen;
    private final @Qualifier("wordReportGenerator") ReportGenerator wordGen;

    @GetMapping(path="/pdf", produces="application/pdf")
    public void pdf(HttpServletResponse resp) throws Exception {
        resp.setHeader("Content-Disposition","attachment; filename=report.pdf");
        List<ReportRowDto> data = service.fetchReportData();
        pdfGen.generate(data, resp.getOutputStream());
    }

    @GetMapping(path="/docx", produces="application/vnd.openxmlformats-officedocument.wordprocessingml.document")
    public void docx(HttpServletResponse resp) throws Exception {
        resp.setHeader("Content-Disposition","attachment; filename=report.docx");
        List<ReportRowDto> data = service.fetchReportData();
        wordGen.generate(data, resp.getOutputStream());
    }
}
