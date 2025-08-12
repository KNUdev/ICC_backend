package ua.knu.knudev.reportmanager.generator;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.springframework.stereotype.Component;
import ua.knu.knudev.icccommon.enums.ReportFields;
import ua.knu.knudev.reportmanagerapi.dto.ReportRowDto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@Component
public class WordReportGenerator {

    public File generate(List<ReportRowDto> data, String fileName) {
        if (!fileName.endsWith(".docx")) {
            fileName += ".docx";
        }

        File file = new File(fileName);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             XWPFDocument document = new XWPFDocument()) {

            XWPFTable table = document.createTable();

            CTTblPr tblPr = table.getCTTbl().getTblPr();
            if (tblPr == null) {
                tblPr = table.getCTTbl().addNewTblPr();
            }
            CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();
            tblWidth.setType(STTblWidth.PCT);
            tblWidth.setW(BigInteger.valueOf(10000));

            List<ReportFields> fields = Arrays.asList(ReportFields.values());
            int cols = fields.size();

            XWPFTableRow header = table.getRow(0);

            for (int i = 0; i < cols; i++) {
                String title = fields.get(i).getValue();
                XWPFTableCell cell;
                if (i == 0) {
                    cell = header.getCell(0);
                } else {
                    cell = header.addNewTableCell();
                }
                cell.setText(title);
                cell.setColor("008000");
            }

            BigInteger[] pctWidths = new BigInteger[]{
                    BigInteger.valueOf(120),
                    BigInteger.valueOf(400),
                    BigInteger.valueOf(400),
                    BigInteger.valueOf(430),
                    BigInteger.valueOf(370),
                    BigInteger.valueOf(320),
                    BigInteger.valueOf(300)
            };
            BigInteger sum = BigInteger.ZERO;
            for (BigInteger w : pctWidths) {
                sum = sum.add(w);
            }

            if (!sum.equals(BigInteger.valueOf(10000))) {
                pctWidths[cols - 1] = pctWidths[cols - 1]
                        .add(BigInteger.valueOf(10000).subtract(sum));
            }

            for (int i = 0; i < cols; i++) {
                XWPFTableCell cell = header.getCell(i);
                configureWidth(pctWidths, i, cell);
            }

            for (ReportRowDto dto : data) {
                XWPFTableRow row = table.createRow();
                for (int i = 0; i < cols; i++) {
                    String text = getFieldStringValue(dto, fields, i);
                    XWPFTableCell cell = row.getCell(i);
                    cell.setText(text);
                    configureWidth(pctWidths, i, cell);
                }
            }

            document.write(fileOutputStream);
        } catch (IOException e) {
            throw new RuntimeException("Word generation failed", e);
        }

        return file;
    }

    private static String getFieldStringValue(ReportRowDto dto, List<ReportFields> fields, int i) {
        return switch (fields.get(i)) {
            case ID -> dto.id().toString();
            case NAME_SURNAME -> dto.name();
            case PHONE_NUMBER -> dto.phoneNumber();
            case EMAIL -> dto.email();
            case POSITION -> dto.position();
            case SALARY -> dto.salary().toString();
            case CONTRACT_VALID_TO -> dto.contractValidTo().toString();
        };
    }

    private void configureWidth(BigInteger[] pctWidths, int i, XWPFTableCell cell) {
        CTTcPr tcPr = cell.getCTTc().getTcPr();
        if (tcPr == null) {
            tcPr = cell.getCTTc().addNewTcPr();
        }
        var cellW = tcPr.isSetTcW() ? tcPr.getTcW() : tcPr.addNewTcW();
        cellW.setType(STTblWidth.PCT);
        cellW.setW(pctWidths[i]);
    }
}
