package ua.knu.knudev.reportmanager.generator;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
import ua.knu.knudev.reportmanagerapi.dto.ReportRowDto;

import java.io.OutputStream;
import java.util.List;

@Component("pdfReportGenerator")
public class PdfReportGenerator implements ReportGenerator {
    @Override
    public void generate(List<ReportRowDto> data, OutputStream out) {
        Document doc = new Document();
        try {
            PdfWriter.getInstance(doc, out);
            doc.open();
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.addCell("Id");
            table.addCell("Name");
            table.addCell("Date");
            table.addCell("Value");
            for (var r : data) {
                table.addCell(r.id().toString());
                table.addCell(r.name());
                table.addCell(r.date().toString());
                table.addCell(r.value().toString());
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
