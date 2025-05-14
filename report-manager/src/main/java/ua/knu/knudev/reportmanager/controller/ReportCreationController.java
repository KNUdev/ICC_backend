package ua.knu.knudev.reportmanager.controller;

import org.springframework.web.bind.annotation.*;
import ua.knu.knudev.reportmanager.service.ReportService;

@RestController
public class ReportCreationController {

    private final ReportService reportService;

    public ReportCreationController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/CreateReport")
    public String reportCreation(@RequestParam("FormatType") String formatType,
                                 @RequestParam("ReportName") String reportName) {

        try{
            switch (formatType) {
                case "csv":
                    reportService.exctractReportToCSV(reportName);
                    break;

                case "excel":
                    reportService.extractReportToExcel(reportName);
                    break;

                default:
                    return "Report format type not supported";
            }
        }catch(Exception e){
            e.printStackTrace();
            return "Unable to create report";
        }
        return "Success";
    }
}
