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
import ua.knu.knudev.reportmanager.enums.ReportFields;
import ua.knu.knudev.reportmanager.exception.ReportGenerationException;
import ua.knu.knudev.reportmanagerapi.api.reportServiceApi;

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
public class reportService implements reportServiceApi {

    private final EmployeeApi employeeApi;
    Workbook excelWorkbook = new XSSFWorkbook();

    private static Sheet excelSheet;

    @Override
    public File extractReportToExcel(String reportName) {
        excelSheet = excelWorkbook.createSheet(reportName);

        createExcelTable(reportName);
        fillExcelTable();

        try {
            return createExcelFile(reportName);
        } catch (IOException e) {
            throw new ReportGenerationException("Failed to create excel file", e);
        }
    }

    @Override
    public File extractReportToCSV(String reportName) {
        try {
            return createCSVFile(reportName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public File createReportOfFormat(String formatType, String reportName) {
        try {
            switch (formatType) {
                case "csv" -> { return extractReportToCSV(reportName); }
                case "excel" -> { return extractReportToExcel(reportName); }
                default -> {
                    throw new RuntimeException("Unsupported format");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

                String specialtyNameEn = employee.specialty().name().getEn();

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
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
        return csvFile;
    }

    private CSVFormat getCSVFormat(){
        String[] headers = {ReportFields.ID.getLabel(),
                ReportFields.NAME_SURNAME.getLabel(),
                ReportFields.PHONE_NUMBER.getLabel(),
                ReportFields.EMAIL.getLabel(),
                ReportFields.POSITION.getLabel(),
                ReportFields.SALARY.getLabel(),
                ReportFields.CONTRACT_VALID_TO.getLabel()};

        return CSVFormat.DEFAULT.builder()
                .setHeader(headers)
                .get();
    }

    private void createExcelTable(String reportName) {
        final int amountOfColumns = ReportFields.values().length;

        int tempIndex = 0;
        for (ReportFields field : ReportFields.values()) {
            excelSheet.setColumnWidth(tempIndex, field.getWidth());

            tempIndex++;
        }

        Row header = excelSheet.createRow(0);

        CellStyle headerStyle = excelWorkbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = (XSSFFont) excelWorkbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell;

        tempIndex = 0;
        for (ReportFields field : ReportFields.values()) {
            headerCell = header.createCell(tempIndex);
            headerCell.setCellValue(field.getLabel());
            headerCell.setCellStyle(headerStyle);

            tempIndex++;
        }
    }

    private void fillExcelTable() {

        CellStyle style = getCellStyle();

        Set<GetEmployeeResponse> employees = employeeApi.getAll();

        Cell cell;
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
            String specialtyNameEn = specialty.name().getEn();

            createExcelStyledCell(row, 0, employee.id(), style);
            createExcelStyledCell(row, 1, fullName, style);
            createExcelStyledCell(row, 2, employee.phoneNumber(), style);
            createExcelStyledCell(row, 3, employee.email(), style);
            createExcelStyledCell(row, 4, specialtyNameEn, style);
            createExcelStyledCell(row, 5, employee.salaryInUAH(), style);
            createExcelStyledCell(row, 6, employee.contractEndDate(), style);
            rowIndex++;
        }
    }

    private void createExcelStyledCell(Row row,int cellIndex, Object value, CellStyle style ) {
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

    private String formatFileNameForFileCreation(String fileName){
        if (!fileName.endsWith(".csv")) {
            fileName += ".csv";
        }

        return fileName;
    }
}
