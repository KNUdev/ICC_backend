package ua.knu.knudev.reportmanager.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ua.knu.knudev.employeemanagerapi.api.EmployeeApi;
import ua.knu.knudev.employeemanagerapi.dto.SpecialtyDto;
import ua.knu.knudev.employeemanagerapi.response.GetEmployeeResponse;
import ua.knu.knudev.icccommon.dto.FullNameDto;
import ua.knu.knudev.icccommon.enums.ReportFields;
import ua.knu.knudev.icccommon.enums.ReportFormat;
import ua.knu.knudev.reportmanager.exception.ReportGenerationException;
import ua.knu.knudev.reportmanager.generator.PdfReportGenerator;
import ua.knu.knudev.reportmanager.generator.WordReportGenerator;
import ua.knu.knudev.reportmanagerapi.api.ReportServiceApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReportService implements ReportServiceApi {

    private final EmployeeApi employeeApi;
    private final DataFetchService dataFetchService;
    private final PdfReportGenerator pdfReportGenerator;
    private final WordReportGenerator wordReportGenerator;
    private static Sheet excelSheet;

    Workbook excelWorkbook = new XSSFWorkbook();

    @Override
    public File createReportOfFormat(ReportFormat formatType, String reportName) {
        try {
            switch (formatType) {
                case ReportFormat.CSV -> {
                    return extractReportToCSV(reportName);
                }
                case ReportFormat.EXCEL -> {
                    return extractReportToExcel(reportName);
                }
                case ReportFormat.PDF -> {
                    return pdfReportGenerator.generate(dataFetchService.getReportRowDto(), reportName);
                }
                case ReportFormat.WORD -> {
                    return wordReportGenerator.generate(dataFetchService.getReportRowDto(), reportName);
                }
                default -> throw new RuntimeException("Unsupported format");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error during generating report");
        }
    }

    private File extractReportToCSV(String reportName) {
        try {
            return createCSVFile(reportName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File extractReportToExcel(String reportName) {
        excelSheet = excelWorkbook.createSheet(reportName);

        createExcelTable();
        fillExcelTable();

        try {
            return createExcelFile(reportName);
        } catch (IOException e) {
            throw new ReportGenerationException("Failed to create excel file");
        }
    }

    private File createCSVFile(String fileName) throws IOException {
        String correctedFileName = formatFileNameForFileCreation(fileName);

        Set<GetEmployeeResponse> employees = employeeApi.getAll();
        File csvFile = new File(correctedFileName);

        CSVFormat csvFormat = getCSVFormat();

        try (
                FileWriter out = new FileWriter(csvFile);
                CSVPrinter csvPrinter = new CSVPrinter(out, csvFormat);
        ) {
            employees.forEach(employee -> {
                FullNameDto name = employee.name();

                String fullName = String.join(" ",
                        name.getLastName(),
                        name.getFirstName(),
                        name.getMiddleName());

                String specialtyNameEn = employee.specialty().name().getEn()
                        + ", " + employee.sector().name().getEn();

                try {
                    csvPrinter.printRecord(
                            employee.id(),
                            fullName,
                            employee.phoneNumber(),
                            employee.email(),
                            specialtyNameEn,
                            employee.salaryInUAH(),
                            employee.contractEndDate());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            csvPrinter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return csvFile;
    }

    private CSVFormat getCSVFormat() {
        String[] headers = {
                ReportFields.ID.getValue(),
                ReportFields.NAME_SURNAME.getValue(),
                ReportFields.PHONE_NUMBER.getValue(),
                ReportFields.EMAIL.getValue(),
                ReportFields.POSITION.getValue(),
                ReportFields.SALARY.getValue(),
                ReportFields.CONTRACT_VALID_TO.getValue()
        };

        return CSVFormat.DEFAULT.builder()
                .setHeader(headers)
                .get();
    }

    private void createExcelTable() {
        int tempIndex = 0;
        for (ReportFields field : ReportFields.values()) {
            excelSheet.setColumnWidth(tempIndex, field.getWidth());

            tempIndex++;
        }

        Row header = excelSheet.createRow(0);

        CellStyle headerStyle = excelWorkbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = (XSSFFont) excelWorkbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 14);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell;

        tempIndex = 0;
        for (ReportFields field : ReportFields.values()) {
            headerCell = header.createCell(tempIndex);
            headerCell.setCellValue(field.getValue());
            headerCell.setCellStyle(headerStyle);

            tempIndex++;
        }
    }

    private void fillExcelTable() {
        CellStyle style = getCellStyle();

        Set<GetEmployeeResponse> employees = employeeApi.getAll();

        int rowIndex = 1;
        for (GetEmployeeResponse employee : employees) {
            Row row = excelSheet.createRow(rowIndex);

            FullNameDto name = employee.name();
            String fullName = String.join(" ",
                    name.getLastName(),
                    name.getFirstName(),
                    name.getMiddleName()
            );

            SpecialtyDto specialty = employee.specialty();
            String specialtyNameEn = specialty.name().getEn() + ", " + employee.sector().name().getEn();

            createExcelStyledCell(row, 0, employee.id(), style);
            createExcelStyledCell(row, 1, fullName, style);
            createExcelStyledCell(row, 2, employee.phoneNumber(), style);
            createExcelStyledCell(row, 3, employee.email(), style);
            createExcelStyledCell(row, 4, specialtyNameEn, style);
            createExcelStyledCell(row, 5, employee.salaryInUAH(), style);
            createExcelStyledCell(row, 6, employee.contractEndDate().toString(), style);
            rowIndex++;
        }
    }

    private void createExcelStyledCell(Row row, int cellIndex, Object value, CellStyle style) {
        Cell cell = row.createCell(cellIndex);

        switch (value) {
            case Number number -> cell.setCellValue(number.doubleValue());
            case String s -> cell.setCellValue(s);
            case LocalDate localDate -> cell.setCellValue(localDate);
            case null, default -> cell.setCellValue(value != null ? value.toString() : "");
        }
        cell.setCellStyle(style);
    }

    private CellStyle getCellStyle() {
        CellStyle style = excelWorkbook.createCellStyle();

        XSSFFont font = (XSSFFont) excelWorkbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        return style;
    }

    private File createExcelFile(String fileName) throws IOException {
        String correctedFileName = formatFileNameForFileCreation(fileName);

        File currentDir = new File(".");
        Path filePath = Paths.get(currentDir.getAbsolutePath(), correctedFileName);
        File excelFile = filePath.toFile();

        FileOutputStream outputStream = new FileOutputStream(excelFile);
        excelWorkbook.write(outputStream);
        excelWorkbook.close();

        return excelFile;
    }

    private String formatFileNameForFileCreation(String fileName) {
        if (!fileName.endsWith(".csv")) {
            fileName += ".csv";
        }

        return fileName;
    }
}
