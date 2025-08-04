package ua.knu.knudev.reportmanager.generator;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
import ua.knu.knudev.icccommon.enums.ReportFields;
import ua.knu.knudev.reportmanagerapi.dto.ReportRowDto;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

@Component
public class PdfReportGenerator {

    public File generate(List<ReportRowDto> data, String fileName) {
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";
        }

        File file = new File(fileName);
        Document doc = new Document();
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            PdfWriter.getInstance(doc, fileOutputStream);
            doc.open();

            PdfPTable table = getPdfPTable(data);
            doc.add(table);

            doc.close();
        } catch (Exception e) {
            throw new RuntimeException("PDF generation failed", e);
        }

        return file;
    }

    private static PdfPTable getPdfPTable(List<ReportRowDto> data) {
        ReportFields[] fields = ReportFields.values();
        PdfPTable table = new PdfPTable(fields.length);
        table.setWidthPercentage(100);

        for (ReportFields f : fields) {
            PdfPCell cell = new PdfPCell();
            cell.setBackgroundColor(Color.green);
            cell.setPhrase(new com.lowagie.text.Phrase(f.getValue()));
            table.addCell(cell);
        }

        for (ReportRowDto row : data) {
            table.addCell(row.id().toString());
            table.addCell(row.name());
            table.addCell(row.phoneNumber());
            table.addCell(row.email());
            table.addCell(row.position());
            table.addCell(row.salary().toString());
            table.addCell(row.contractValidTo().toString());
        }
        return table;
    }
}
