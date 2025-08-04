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
import ua.knu.knudev.reportmanagerapi.dto.ReportRowDto;
import ua.knu.knudev.reportmanager.enums.ReportFields;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@Component
public class WordReportGenerator implements ReportGenerator {

    @Override
    public void generate(List<ReportRowDto> data, OutputStream outputStream) {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFTable table = document.createTable();
            CTTblPr tblPr = table.getCTTbl().getTblPr();
            if (tblPr == null) tblPr = table.getCTTbl().addNewTblPr();
            CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();
            tblWidth.setType(STTblWidth.PCT);
            tblWidth.setW(BigInteger.valueOf(10000));

            List<ReportFields> fields = Arrays.asList(ReportFields.values());
            int cols = fields.size();
            XWPFTableRow header = table.getRow(0);
            for (int i = 0; i < cols; i++) {
                String title = fields.get(i).getValue();
                if (i == 0) header.getCell(0).setText(title);
                else        header.addNewTableCell().setText(title);
            }


            BigInteger[] pctWidths = new BigInteger[] {
                    BigInteger.valueOf(  120),
                    BigInteger.valueOf(400),
                    BigInteger.valueOf(400),
                    BigInteger.valueOf(430),
                    BigInteger.valueOf(370),
                    BigInteger.valueOf(320),
                    BigInteger.valueOf(300)
            };
            BigInteger sum = BigInteger.ZERO;
            for (BigInteger w : pctWidths) sum = sum.add(w);
            if (!sum.equals(BigInteger.valueOf(10000))) {
                // автоматично підлаштуємо останній
                pctWidths[cols - 1] = pctWidths[cols - 1]
                        .add(BigInteger.valueOf(10000).subtract(sum));
            }

            for (int i = 0; i < cols; i++) {
                XWPFTableCell cell = header.getCell(i);
                CTTcPr tcPr = cell.getCTTc().getTcPr();
                if (tcPr == null) tcPr = cell.getCTTc().addNewTcPr();
                var cellW = tcPr.isSetTcW() ? tcPr.getTcW() : tcPr.addNewTcW();
                cellW.setType(STTblWidth.PCT);
                cellW.setW(pctWidths[i]);
            }

            for (ReportRowDto dto : data) {
                XWPFTableRow row = table.createRow();
                for (int i = 0; i < cols; i++) {
                    String text;
                    switch (fields.get(i)) {
                        case ID               -> text = dto.id().toString();
                        case NAME_SURNAME     -> text = dto.nameSurname();
                        case PHONE_NUMBER     -> text = dto.phoneNumber();
                        case EMAIL            -> text = dto.email();
                        case POSITION         -> text = dto.position();
                        case SALARY           -> text = dto.salary().toString();
                        case CONTRACT_VALID_TO-> text = dto.contractValidTo().toString();
                        default               -> text = "";
                    }
                    XWPFTableCell cell = row.getCell(i);
                    cell.setText(text);

                    CTTcPr tcPr = cell.getCTTc().getTcPr();
                    if (tcPr == null) tcPr = cell.getCTTc().addNewTcPr();
                    var cellW = tcPr.isSetTcW() ? tcPr.getTcW() : tcPr.addNewTcW();
                    cellW.setType(STTblWidth.PCT);
                    cellW.setW(pctWidths[i]);
                }
            }

            document.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Word generation failed", e);
        }
    }
}
