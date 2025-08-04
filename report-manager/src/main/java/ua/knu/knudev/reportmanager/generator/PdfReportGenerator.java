package ua.knu.knudev.reportmanager.generator;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
import ua.knu.knudev.reportmanagerapi.dto.ReportRowDto;
import ua.knu.knudev.reportmanager.enums.ReportFields;

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

            ReportFields[] fields = ReportFields.values();
            PdfPTable table = new PdfPTable(fields.length);
            table.setWidthPercentage(100);

            for (ReportFields f : fields) {
                table.addCell(f.getValue());
            }

            for (ReportRowDto row : data) {
                table.addCell(row.id().toString());
                table.addCell(row.nameSurname());
                table.addCell(row.phoneNumber());
                table.addCell(row.email());
                table.addCell(row.position());
                table.addCell(row.salary().toString());
                table.addCell(row.contractValidTo().toString());
            }

            doc.add(table);
        } catch (Exception e) {
            throw new RuntimeException("PDF generation failed", e);
        } finally {
            if (doc.isOpen()) {
                doc.close();
            }
        }
    }
}
