package ua.knu.knudev.reportmanager.generator;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
import ua.knu.knudev.reportmanagerapi.dto.ReportRowDto;

import java.io.OutputStream;
import java.util.List;

@Component
public class PdfReportGenerator implements ReportGenerator {
    @Override
    public void generate(List<ReportRowDto> data, OutputStream out) {
        Document doc = new Document();
        try {
            PdfWriter.getInstance(doc, out);
            doc.open();
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.addCell("Ід");
            table.addCell("ПІБ");
            table.addCell("Дата");
            table.addCell("Значення");
            for (var row : data) {
                table.addCell(row.id().toString());
                table.addCell(row.fullName());
                table.addCell(row.date().toString());
                table.addCell(row.points().toString());
            }
            doc.add(table);
            doc.close();
        } catch (Exception e) {
            throw new RuntimeException("PDF generation failed", e);
        } finally {
            if (doc.isOpen()) {
                doc.close();
            }
        }
    }
}
