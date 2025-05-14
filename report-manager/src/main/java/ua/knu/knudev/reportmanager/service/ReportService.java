package ua.knu.knudev.reportmanager.service;

import org.springframework.stereotype.Service;
import ua.knu.knudev.employeemanager.repository.EmployeeRepository;import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.commons.csv.CSVFormat;

import java.io.*;

@Service
public class ReportService {
    private final EmployeeRepository employeeRepository;

    public ReportService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    final Workbook excelWorkbook = new XSSFWorkbook();
    private Sheet excelSheet;

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

    public void exctractReportToCSV(String reportName) {
        try {
            createCSVFile("testCSV");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createCSVFile(String fileName) throws IOException {
        String[] headers = {"Id", "Ім'я по батькові", "Посада", "Бали", "Дата"};

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(headers)
                .build();

        try (
                FileWriter out = new FileWriter(fileName + ".csv");
                CSVPrinter csvPrinter = new CSVPrinter(out, csvFormat)
        ) {
            csvPrinter.printRecord("1", "John Smith", "Senior Engineer", "0,5", "23.05.2025");
            csvPrinter.printRecord("2", "Tom Adams", "Junior Engineer", "4,5", "15.01.2023");
            csvPrinter.printRecord("3", "Mike Thompson", "Senior Architect", "1,2", "04.08.2021");
            csvPrinter.printRecord("4", "John Lemon", "Consultant", "0,7", "31.12.2024");
        };
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
        headerCell.setCellValue("Id");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Ім'я по батькові");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue("Посада");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(3);
        headerCell.setCellValue("Бали");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(4);
        headerCell.setCellValue("Дата");
        headerCell.setCellStyle(headerStyle);
    }

    private void fillExcelTable() {
        Row row = excelSheet.createRow(2);

        CellStyle style = excelWorkbook.createCellStyle();

        XSSFFont font = (XSSFFont) excelWorkbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);

        Cell cell = row.createCell(0);
        cell.setCellValue("1");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("John Smith");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("Senior Engineer");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("0,5");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("23.05.2025");
        cell.setCellStyle(style);
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
