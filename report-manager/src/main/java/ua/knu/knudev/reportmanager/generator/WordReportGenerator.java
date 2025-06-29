package ua.knu.knudev.reportmanager.generator;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.springframework.stereotype.Component;
import ua.knu.knudev.reportmanagerapi.dto.ReportRowDto;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Component("wordReportGenerator")
public class WordReportGenerator implements ReportGenerator {
    @Override
    public void generate(List<ReportRowDto> data, OutputStream outputStream) {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFTable table = document.createTable();
            var headerRow = table.getRow(0);
            headerRow.getCell(0).setText("Id");
            headerRow.addNewTableCell().setText("Name");
            headerRow.addNewTableCell().setText("Date");
            headerRow.addNewTableCell().setText("Value");
            for (var rowDto : data) {
                var row = table.createRow();
                row.getCell(0).setText(rowDto.id().toString());
                row.getCell(1).setText(rowDto.name());
                row.getCell(2).setText(rowDto.date().toString());
                row.getCell(3).setText(rowDto.value().toString());
            }
            document.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Word generation failed", e);
        }
    }
}
