package ua.knu.knudev.reportmanager.generator;

import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Component;
import ua.knu.knudev.reportmanagerapi.dto.ReportRowDto;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Component("wordReportGenerator")
public class WordReportGenerator implements ReportGenerator {
    @Override
    public void generate(List<ReportRowDto> data, OutputStream out){
        try (XWPFDocument doc = new XWPFDocument()) {
            XWPFTable table = doc.createTable();
            var h = table.getRow(0);
            h.getCell(0).setText("Id");
            h.addNewTableCell().setText("Name");
            h.addNewTableCell().setText("Date");
            h.addNewTableCell().setText("Value");
            for (var r : data) {
                var row = table.createRow();
                row.getCell(0).setText(r.id().toString());
                row.getCell(1).setText(r.name());
                row.getCell(2).setText(r.date().toString());
                row.getCell(3).setText(r.value().toString());
            }
            doc.write(out);
        } catch (IOException e) {
            throw new RuntimeException("Word generation failed", e);
        }
    }
}
