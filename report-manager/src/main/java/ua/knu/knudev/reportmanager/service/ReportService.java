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

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class ReportService {

private final EmployeeApi employeeApi;
    Workbook excelWorkbook = new XSSFWorkbook();
    private static Sheet excelSheet;

    public void extractReportToExcel(String reportName) {
        excelSheet = excelWorkbook.createSheet(reportName);

        createExcelTable(reportName);
        fillExcelTable();

        try {
            createExcelFile(reportName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void extractReportToCSV(String reportName) {
        try {
            createCSVFile(reportName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createCSVFile(String fileName) throws IOException {
        String[] headers = {"ІД", "Ім'я та прізвище", "Номер телефону", "Пошта", "Посада", "Зарплата", "Дата закінчення контракту"};

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(headers)
                .build();

        try (
                FileWriter out = new FileWriter(fileName + ".csv");
                CSVPrinter csvPrinter = new CSVPrinter(out, csvFormat)
        ) {
            Set<GetEmployeeResponse> employees = employeeApi.getAll();

            for(GetEmployeeResponse employee : employees) {
                FullNameDto name = employee.name();
                String fullName = name.getLastName() + " " + name.getFirstName() + " " + name.getMiddleName();

                SpecialtyDto specialty = employee.specialty();
                String specialtyNameEn = specialty.name().getEn();

                csvPrinter.printRecord(
                        employee.id(),
                        fullName,
                        employee.phoneNumber(),
                        employee.email(),
                        specialtyNameEn,
                        employee.salaryInUAH(),
                        employee.contractEndDate());
            }
        }
    }

    private void createExcelTable(String reportName) {
        excelSheet.setColumnWidth(0, 1500);
        excelSheet.setColumnWidth(1, 12000);
        excelSheet.setColumnWidth(2, 8000);
        excelSheet.setColumnWidth(3, 3000);
        excelSheet.setColumnWidth(4, 4000);

        Row header = excelSheet.createRow(0);

        CellStyle headerStyle = excelWorkbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = (XSSFFont) excelWorkbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("ІД");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Ім'я та прізвище");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue("Номер телефону");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(3);
        headerCell.setCellValue("Пошта");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(4);
        headerCell.setCellValue("Посада");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(5);
        headerCell.setCellValue("Зарплата");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(6);
        headerCell.setCellValue("Дата закінчення контракту");
        headerCell.setCellStyle(headerStyle);
    }

    private void fillExcelTable() {

        CellStyle style = getCellStyle();

        Set<GetEmployeeResponse> employees = employeeApi.getAll();

        Cell cell;
        int rowIndex = 1;
        for(GetEmployeeResponse employee : employees) {
            Row row = excelSheet.createRow(rowIndex);

            cell = row.createCell(0);
            cell.setCellValue(String.valueOf(employee.id()));
            cell.setCellStyle(style);

            FullNameDto name = employee.name();
            String fullName = name.getLastName() + " " + name.getFirstName() + " " + name.getMiddleName();

            cell = row.createCell(1);
            cell.setCellValue(fullName);
            cell.setCellStyle(style);

            cell = row.createCell(2);
            cell.setCellValue(employee.phoneNumber());
            cell.setCellStyle(style);

            cell = row.createCell(3);
            cell.setCellValue(employee.email());
            cell.setCellStyle(style);

            SpecialtyDto specialty = employee.specialty();
            String specialtyNameEn = specialty.name().getEn();

            cell = row.createCell(4);
            cell.setCellValue(specialtyNameEn);
            cell.setCellStyle(style);

            cell = row.createCell(5);
            cell.setCellValue(employee.salaryInUAH());
            cell.setCellStyle(style);

            cell = row.createCell(6);
            cell.setCellValue(employee.contractEndDate());
            cell.setCellStyle(style);

            rowIndex++;
        }
    }

    private CellStyle getCellStyle(){
        CellStyle style = excelWorkbook.createCellStyle();

        XSSFFont font = (XSSFFont) excelWorkbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        return style;
    }

    private void createExcelFile(String fileName) throws IOException {
        File currentDir = new File(".");
        String path = currentDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + fileName + ".xlsx";

        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        excelWorkbook.write(outputStream);
        excelWorkbook.close();
    }
}
